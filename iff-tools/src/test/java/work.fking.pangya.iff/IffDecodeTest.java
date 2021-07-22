package work.fking.pangya.iff;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import work.fking.pangya.iff.model.IffBall;
import work.fking.pangya.iff.model.IffCharacter;
import work.fking.pangya.iff.model.IffClubSet;
import work.fking.pangya.iff.model.IffCourse;
import work.fking.pangya.iff.model.IffHairStyle;
import work.fking.pangya.iff.model.IffItem;
import work.fking.pangya.iff.model.IffPart;

import java.io.IOException;
import java.io.InputStream;

@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class IffDecodeTest {

    private static final Logger LOGGER = LogManager.getLogger(IffDecodeTest.class);

    private ByteBuf loadResource(String resourcePath) throws IOException {
        InputStream resource = getClass().getResourceAsStream(resourcePath);

        if (resource == null) {
            throw new IllegalStateException("Missing resource");
        }
        return Unpooled.wrappedBuffer(resource.readAllBytes());
    }

    @Test
    public void decode_iff_ball() throws IOException {
        ByteBuf buffer = loadResource("/iff/Ball.iff");

        Iff.decode(buffer, IffBall::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_character() throws IOException {
        ByteBuf buffer = loadResource("/iff/Character.iff");

        Iff.decode(buffer, IffCharacter::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_clubset() throws IOException {
        ByteBuf buffer = loadResource("/iff/ClubSet.iff");

        Iff.decode(buffer, IffClubSet::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_course() throws IOException {
        ByteBuf buffer = loadResource("/iff/Course.iff");

        Iff.decode(buffer, IffCourse::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_hairstyle() throws IOException {
        ByteBuf buffer = loadResource("/iff/HairStyle.iff");

        Iff.decode(buffer, IffHairStyle::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_item() throws IOException {
        ByteBuf buffer = loadResource("/iff/Item.iff");

        Iff.decode(buffer, IffItem::decode).entries().forEach(LOGGER::debug);
    }

    @Test
    public void decode_iff_part() throws IOException {
        ByteBuf buffer = loadResource("/iff/Part.iff");

        Iff.decode(buffer, IffPart::decode).entries().forEach(LOGGER::debug);
    }
}
