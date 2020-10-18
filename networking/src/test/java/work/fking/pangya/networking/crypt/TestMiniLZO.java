package work.fking.pangya.networking.crypt;

import io.netty.buffer.ByteBufUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import work.fking.pangya.networking.lzo.MInt;
import work.fking.pangya.networking.lzo.MiniLZO;

@TestInstance(Lifecycle.PER_CLASS)
public class TestMiniLZO {

    @Test
    public void test_compress() {
        byte[] bytes = ByteBufUtil.decodeHexDump("9600ffe0f50500000000");
        MInt mInt = new MInt();
        byte[] out = new byte[128];
        int result = MiniLZO.lzo1x_1_compress(bytes, bytes.length, out, mInt, new int[32768]);
        String hexDump = ByteBufUtil.hexDump(out, 0, mInt.v);
        Assertions.assertEquals("1b9600ffe0f50500000000110000", hexDump);
    }
}
