package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.IffObjectList;
import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.networking.protocol.OutboundPacket;

import java.util.List;

public class CharacterRosterPacket implements OutboundPacket {

    private static final int ID = 0x70;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        var iffList = IffObjectList.from(List.of(PangCharacter.mock()));
        iffList.encode(target);
    }
}
