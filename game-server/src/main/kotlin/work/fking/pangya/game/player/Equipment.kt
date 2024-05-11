package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffType.BALL
import work.fking.pangya.game.model.IffType.CLUBSET
import work.fking.pangya.game.model.IffType.EQUIPITEM_ITEM
import work.fking.pangya.game.model.iffTypeFromId

const val EQUIPPED_ITEMS_SIZE = 10

class Equipment(
    val itemIffIds: IntArray = IntArray(10),
    characterUid: Int = 0,
    caddieUid: Int = 0,
    clubSetUid: Int = 0,
    cometIffId: Int = 0
) {
    var characterUid = characterUid
        private set
    var caddieUid = caddieUid
        private set
    var clubSetUid = clubSetUid
        private set
    var cometIffId = cometIffId
        private set

    fun updateEquippedItems(itemIffIds: IntArray) {
        require(itemIffIds.size == EQUIPPED_ITEMS_SIZE) { "Equipped item iff ids invalid length" }
        for (i in itemIffIds.indices) {
            val iffId = itemIffIds[i]
            require(iffId == 0 || iffTypeFromId(iffId) == EQUIPITEM_ITEM) { "Iff object $iffId is not an item" }
            this.itemIffIds[i] = iffId
        }
    }

    fun equipClubSet(clubSet: Item) {
        require(clubSet.iffTypeId() == CLUBSET) { "Item is not a clubSet" }
        clubSetUid = clubSet.uid
    }

    fun equipComet(comet: Item) {
        require(comet.iffTypeId() == BALL) { "Item is not a comet" }
        cometIffId = comet.iffId
    }

    fun equipCharacter(character: Character) {
        characterUid = character.uid
    }

    fun equipCaddie(caddie: Caddie) {
        caddieUid = caddie.uid
    }

    fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(caddieUid)
            writeIntLE(characterUid)
            writeIntLE(clubSetUid)
            writeIntLE(cometIffId)

            for (itemIffId in itemIffIds) {
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
