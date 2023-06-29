package work.fking.pangya.game.player

import io.netty.channel.Channel
import work.fking.pangya.game.ServerChannel
import java.util.Objects

class Player(private val channel: Channel, private val uid: Int, private val connectionId: Int, private val nickname: String) {
    private val inventory = Inventory()
    private val equipment = Equipment(this)
    private val characterRoster = CharacterRoster()
    private val caddieRoster = CaddieRoster()

    private val rank = 30
    private var experience = 0
    private var pangBalance = 10000
    private var cookieBalance = 0
    private var currentChannel: ServerChannel? = null

    fun channel(): Channel {
        return channel
    }

    fun uid(): Int {
        return uid
    }

    fun connectionId(): Int {
        return connectionId
    }

    fun nickname(): String {
        return nickname
    }

    fun inventory(): Inventory {
        return inventory
    }

    fun equipment(): Equipment {
        return equipment
    }

    fun characterRoster(): CharacterRoster {
        return characterRoster
    }

    fun caddieRoster(): CaddieRoster {
        return caddieRoster
    }

    fun rank(): Int {
        return rank
    }

    fun experience(): Int {
        return experience
    }

    fun addExperience(amount: Int) {
        require(amount >= 0) { "Cannot add negative experience" }
        experience += amount
    }

    fun pangBalance(): Int {
        return pangBalance
    }

    fun updatePangBalance(delta: Int) {
        pangBalance += delta
    }

    fun cookieBalance(): Int {
        return cookieBalance
    }

    fun updateCookieBalance(delta: Int) {
        cookieBalance += delta
    }

    fun currentChannel(): ServerChannel? {
        return currentChannel
    }

    fun setCurrentChannel(currentChannel: ServerChannel) {
        require(this.currentChannel == null) { "Player is already in a channel" }
        this.currentChannel = currentChannel
    }

    fun equippedCharacter(): Character {
        return characterRoster.findByUid(equipment.equippedCharacterUid()) ?: throw IllegalStateException("Player does not have a equipped character")
    }

    fun activeCaddie(): Caddie? {
        return caddieRoster.findByUid(equipment.equippedCaddieUid())
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
