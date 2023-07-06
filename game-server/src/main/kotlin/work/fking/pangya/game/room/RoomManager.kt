package work.fking.pangya.game.room

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class RoomManager {
    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(Room::class.java)
    }

    private val idSequence = AtomicInteger()
    private val rooms = HashMap<Int, Room>()

    fun findRoom(roomId: Int): Room? {
        return rooms[roomId]
    }

    fun createRoom(
        name: String,
        password: String? = null,
        roomType: RoomType,
        course: Course,
        holeMode: HoleMode,
        holeCount: Int,
        maxPlayers: Int,
        shotTime: Int,
        gameTime: Int,
        artifactIffId: Int,
        naturalWind: Boolean
    ): Room {
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
            ),
            playerLeaveListener = this::onPlayerRoomLeave
        )

        rooms[id] = room
        LOGGER.debug("Room $id created")
        return room
    }

    private fun onPlayerRoomLeave(room: Room, player: RoomPlayer) {
        if (room.isEmpty()) {
            rooms.remove(room.id)
            LOGGER.debug("Destroying room ${room.id} as it was empty")
        }
    }
}