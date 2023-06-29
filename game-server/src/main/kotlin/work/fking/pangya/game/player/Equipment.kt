package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId

class Equipment(private val player: Player) {

    companion object {
        private const val EQUIPPED_ITEMS_SIZE = 10
    }

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
            require(iffTypeFromId(itemIffIds[i]) == IffObject.TYPE_ITEM) { "Iff object " + itemIffIds[i] + " is not an item" }
            equippedItemIffIds[i] = itemIffIds[i]
        }
    }

    fun equipClubSet(clubSet: Item) {
        require(clubSet.iffTypeId() == IffObject.TYPE_CLUBSET) { "Item is not a clubSet" }
        // sanity check to see if we actually own the item
        if (player.inventory().existsByUid(clubSet.uid)) {
            equippedClubSetUid = clubSet.uid
        }
    }

    fun equipComet(comet: Item) {
        require(comet.iffTypeId() == IffObject.TYPE_BALL) { "Item is not a comet" }
        // sanity check to see if we actually own the item
        if (player.inventory().existsByUid(comet.uid)) {
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
