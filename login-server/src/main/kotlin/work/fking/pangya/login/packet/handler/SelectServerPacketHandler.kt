package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.task.HandoverTask

class SelectServerPacketHandler : ClientPacketHandler {

    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        require(player.hasBaseCharacter || player.pickedCharacterIffId != null) { "Cannot proceed with handover, player didn't pick a default character" }
        requireNotNull(player.nickname) { "Cannot proceed with handover, player doesn't have a nickname set" }

        val serverId = packet.readShortLE().toInt()
        server.submitTask(HandoverTask(server, player, serverId))
    }
}
