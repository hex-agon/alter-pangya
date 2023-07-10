package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.LobbyReplies
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.HoleMode
import work.fking.pangya.game.room.RoomType

class JoinLobbyPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        player.writeAndFlush(LobbyReplies.ackJoin())

        val currentChannel = player.currentChannel!!
        val roomManager = currentChannel.roomManager
        val roomVersus = roomManager.createRoom(
            name = "Versus room",
            roomType = RoomType.VERSUS,
            course = Course.BLUE_MOON,
            holeMode = HoleMode.FRONT,
            holeCount = 3,
            maxPlayers = 2,
            shotTime = 40000,
            gameTime = 0,
            artifactIffId = 0,
            naturalWind = true
        )
        val roomTournament = roomManager.createRoom(
            name = "Tournament room",
            roomType = RoomType.TOURNAMENT,
            course = Course.BLUE_LAGOON,
            holeMode = HoleMode.FRONT,
            holeCount = 18,
            maxPlayers = 30,
            shotTime = 40000,
            gameTime = 1800000,
            artifactIffId = 436208044,
            naturalWind = false
        )
        val roomLounge = roomManager.createRoom(
            name = "Lounge room",
            roomType = RoomType.LOUNGE,
            course = Course.BLUE_LAGOON,
            holeMode = HoleMode.FRONT,
            holeCount = 1,
            maxPlayers = 30,
            shotTime = 0,
            gameTime = 0,
            artifactIffId = 0,
            naturalWind = false
        )
        player.writeAndFlush(
            RoomReplies.list(
                listOf(
                    roomVersus,
                    roomTournament,
                    roomLounge
                )
            )
        )
    }
}
