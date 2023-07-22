package work.fking.pangya.game.player

import io.netty.channel.Channel
import work.fking.pangya.game.ServerChannel
import work.fking.pangya.game.room.Room
import java.util.Objects

class Player(
    private val channel: Channel,
    val uid: Int,
    val connectionId: Int,
    val username: String,
    val nickname: String,
    val wallet: PlayerWallet = PlayerWallet(),
    val characterRoster: CharacterRoster = CharacterRoster(),
    val caddieRoster: CaddieRoster = CaddieRoster(),
    val inventory: Inventory = Inventory(),
    val equipment: Equipment = Equipment(),
    var statistics: PlayerStatistics = PlayerStatistics(),
    val achievements: PlayerAchievements = createPlayerAchievements()
) {
    var currentChannel: ServerChannel? = null
        set(value) {
            require(currentChannel == null) { "Player is already in a channel" }
            field = value
        }
    var currentRoom: Room? = null
        set(value) {
            if (value != null) require(currentRoom == null) { "Player is already in a room" }
            field = value
        }

    fun equippedCharacter(): Character {
        return characterRoster.findByUid(equipment.equippedCharacterUid) ?: throw IllegalStateException("Player does not have a equipped character")
    }

    fun equippedCaddie(): Caddie {
        return caddieRoster.findByUid(equipment.equippedCaddieUid) ?: nullCaddie()
    }

    fun equippedClubSet(): Item? {
        return inventory.findByUid(equipment.equippedClubSetUid)
    }

    fun write(message: Any) {
        channel.write(message, channel.voidPromise())
    }

    fun writeAndFlush(message: Any) {
        channel.writeAndFlush(message)
    }

    fun flush() {
        channel.flush()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val player = other as Player
        return uid == player.uid
    }

    override fun hashCode(): Int {
        return Objects.hash(uid)
    }
}
