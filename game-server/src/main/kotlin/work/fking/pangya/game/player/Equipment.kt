package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IFF_TYPE_BALL
import work.fking.pangya.game.model.IFF_TYPE_CADDIE
import work.fking.pangya.game.model.IFF_TYPE_CLUBSET
import work.fking.pangya.game.model.IFF_TYPE_ITEM
import work.fking.pangya.game.model.iffTypeFromId

const val EQUIPPED_ITEMS_SIZE = 10

class Equipment(private val player: Player) {

    private val equippedItemIffIds = IntArray(EQUIPPED_ITEMS_SIZE)
    private var equippedClubSetUid = 0
    private var equippedCometIffId = 0
    private var equippedCharacterUid = 0
    private var equippedCaddieUid = 0

    fun equippedCharacterUid(): Int {
        return equippedCharacterUid
    }

    fun equippedCaddieUid(): Int {
        return equippedCaddieUid
    }

    fun updateEquippedItems(itemIffIds: IntArray) {
        require(itemIffIds.size == EQUIPPED_ITEMS_SIZE) { "Equipped item iff ids invalid length" }
        for (i in itemIffIds.indices) {
            val iffId = itemIffIds[i]
            require(iffId == 0 || iffTypeFromId(iffId) == IFF_TYPE_ITEM) { "Iff object $iffId is not an item" }
            equippedItemIffIds[i] = iffId
        }
    }

    fun equipClubSet(clubSet: Item) {
        require(clubSet.iffTypeId() == IFF_TYPE_CLUBSET) { "Item is not a clubSet" }
        // sanity check to see if we actually own the item
        if (player.inventory.existsByUid(clubSet.uid)) {
            equippedClubSetUid = clubSet.uid
        }
    }

    fun equipComet(comet: Item) {
        require(comet.iffTypeId() == IFF_TYPE_BALL) { "Item is not a comet" }
        // sanity check to see if we actually own the item
        if (player.inventory.existsByUid(comet.uid)) {
            equippedCometIffId = comet.iffId
        }
    }

    fun equipCharacter(character: Character) {
        equippedCharacterUid = character.uid
    }

    fun equipCaddie(caddie: Caddie) {
        equippedCaddieUid = caddie.uid
    }

    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(equippedCaddieUid)
        buffer.writeIntLE(equippedCharacterUid)
        buffer.writeIntLE(equippedClubSetUid)
        buffer.writeIntLE(equippedCometIffId)

        for (itemIffId in equippedItemIffIds) {
            buffer.writeIntLE(itemIffId)
        }
        buffer.writeIntLE(0) // background
        buffer.writeIntLE(0) // frame
        buffer.writeIntLE(0) // sticker
        buffer.writeIntLE(0) // slot
        buffer.writeIntLE(0)
        buffer.writeIntLE(0) // title
        buffer.writeIntLE(0) // skinBg
        buffer.writeIntLE(0) // skinFrame
        buffer.writeIntLE(0) // skinSticker
        buffer.writeIntLE(0) // skinSlot
        buffer.writeIntLE(0)
        buffer.writeIntLE(0) // title
        buffer.writeIntLE(0) // mascot
        buffer.writeIntLE(0) // poster1
        buffer.writeIntLE(0) // poster2
    }
}
