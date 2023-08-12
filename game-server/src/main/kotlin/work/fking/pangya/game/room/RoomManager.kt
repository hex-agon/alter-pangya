package work.fking.pangya.game.room

import org.slf4j.LoggerFactory
import work.fking.pangya.game.room.RoomState.PENDING_REMOVAL
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

private val LOGGER = LoggerFactory.getLogger(Room::class.java)

private val executorService = Executors.newSingleThreadScheduledExecutor()

class RoomManager {
    private val idSequence = AtomicInteger()
    private val roomsLock = ReentrantReadWriteLock()
    private val rooms = LinkedHashMap<Int, Room>()

    init {
        executorService.scheduleAtFixedRate(this::tickRooms, 1, 1, TimeUnit.SECONDS)
    }

    fun activeRooms(): List<Room> {
        roomsLock.read {
            return rooms.values.filter { it.state != PENDING_REMOVAL }
        }
    }

    fun findRoom(roomId: Int): Room? {
        roomsLock.read {
            return rooms[roomId]
        }
    }

    fun createRoom(
        name: String,
        password: String? = null,
        roomType: RoomType,
        course: Course,
        holeMode: HoleMode,
        holeCount: Int,
        maxPlayers: Int,
        shotTime: Duration,
        gameTime: Duration,
        artifactIffId: Int,
        naturalWind: Boolean
    ): Room {
        roomsLock.write {
            val id = idSequence.getAndIncrement()
            val room = Room(
                id = id,
                settings = RoomSettings(
                    name = name,
                    password = password,
                    roomType = roomType,
                    course = course,
                    holeMode = holeMode,
                    holeCount = holeCount,
                    maxPlayers = maxPlayers,
                    shotTime = shotTime,
                    gameTime = gameTime,
                    artifactIffId = artifactIffId,
                    naturalWind = naturalWind,
                )
            )
            rooms[id] = room
            LOGGER.debug("Room $id created")
            return room
        }
    }

    private fun tickRooms() {
        roomsLock.write {
            val iterator = rooms.values.iterator()

            while (iterator.hasNext()) {
                val room = iterator.next()

                if (room.state == PENDING_REMOVAL) {
                    LOGGER.debug("Room [${room.id}] destroyed")
                    iterator.remove()
                    continue
                }
                runCatching { room.tick() }.onFailure { LOGGER.error("Error while ticking room [${room.id}]", it) }
            }
        }
    }
}