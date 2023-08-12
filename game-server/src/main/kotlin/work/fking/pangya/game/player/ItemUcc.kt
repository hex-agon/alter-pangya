package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.writeFixedSizeString

class ItemUcc(val unknown: Int, val id: String, val name: String, val copiedFrom: String, val copiedFromUid: Int)

fun ByteBuf.write(ucc: ItemUcc) {
    writeFixedSizeString(ucc.name, 40) // name
    writeByte(0)
    writeFixedSizeString(ucc.id, 9) // string id (this is what the client will request from the webserver)
    writeByte(ucc.unknown) // if this is not 1, client does not init the ucc
    writeZero(2)
    writeFixedSizeString(ucc.copiedFrom, 22) // the name of who created this copy
    writeIntLE(ucc.copiedFromUid) // the uid of who created this copy
}

fun nullItemUcc() = ItemUcc(
    unknown = 0,
    id = "",
    name = "",
    copiedFrom = "",
    copiedFromUid = 0
)