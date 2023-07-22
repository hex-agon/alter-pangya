package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IFF_TYPE_BALL
import work.fking.pangya.game.model.IFF_TYPE_CLUBSET
import work.fking.pangya.game.model.IFF_TYPE_EQUIPITEM_ITEM
import work.fking.pangya.game.model.iffTypeFromId

const val EQUIPPED_ITEMS_SIZE = 10

class Equipment(
    private val equippedItemIffIds: IntArray = IntArray(10),
    equippedClubSetUid: Int = 0,
    equippedCometIffId: Int = 0,
    equippedCharacterUid: Int = 0,
    equippedCaddieUid: Int = 0
) {

    var equippedClubSetUid = equippedClubSetUid
        private set
    var equippedCometIffId = equippedCometIffId
        private set
    var equippedCharacterUid = equippedCharacterUid
        private set
    var equippedCaddieUid = equippedCaddieUid
        private set

    fun updateEquippedItems(itemIffIds: IntArray) {
        require(itemIffIds.size == EQUIPPED_ITEMS_SIZE) { "Equipped item iff ids invalid length" }
        for (i in itemIffIds.indices) {
            val iffId = itemIffIds[i]
            require(iffId == 0 || iffTypeFromId(iffId) == IFF_TYPE_EQUIPITEM_ITEM) { "Iff object $iffId is not an item" }
            equippedItemIffIds[i] = iffId
        }
    }

    fun equipClubSet(clubSet: Item) {
        require(clubSet.iffTypeId() == IFF_TYPE_CLUBSET) { "Item is not a clubSet" }
        equippedClubSetUid = clubSet.uid
    }

    fun equipComet(comet: Item) {
        require(comet.iffTypeId() == IFF_TYPE_BALL) { "Item is not a comet" }
        equippedCometIffId = comet.iffId
    }

    fun equipCharacter(character: Character) {
        equippedCharacterUid = character.uid
    }

    fun equipCaddie(caddie: Caddie) {
        equippedCaddieUid = caddie.uid
    }

    fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(equippedCaddieUid)
            writeIntLE(equippedCharacterUid)
            writeIntLE(equippedClubSetUid)
            writeIntLE(equippedCometIffId)

            for (itemIffId in equippedItemIffIds) {
                writeIntLE(itemIffId)
            }
            writeIntLE(0) // background
            writeIntLE(0) // frame
            writeIntLE(0) // sticker
            writeIntLE(0) // slot
            writeIntLE(0)
            writeIntLE(0) // title
            writeIntLE(0) // skinBg
            writeIntLE(0) // skinFrame
            writeIntLE(0) // skinSticker
            writeIntLE(0) // skinSlot
            writeIntLE(0)
            writeIntLE(0) // title
            writeIntLE(0) // mascot
            writeIntLE(0) // poster1
            writeIntLE(0) // poster2
        }
    }
}
