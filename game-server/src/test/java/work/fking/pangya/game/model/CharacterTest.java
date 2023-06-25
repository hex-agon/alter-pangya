package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import work.fking.pangya.game.player.Character;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing the PangCharacter class")
public class CharacterTest {

    private ByteBuf loadResource(String resourcePath) throws IOException {
        InputStream resource = getClass().getResourceAsStream(resourcePath);

        if (resource == null) {
            throw new IllegalStateException("Missing resource");
        }
        return Unpooled.wrappedBuffer(resource.readAllBytes());
    }

    @Test
    @DisplayName("Should correctly decode a pangya character from a binary stream")
    public void test_decrypt() throws IOException {
        var buffer = loadResource("/pangya_character.dat");

        var character = Character.decode(buffer);
        assertEquals(67108872, character.iffId());
        assertEquals(262513, character.uid());
        assertEquals(2, character.hairColor());
        assertArrayEquals(new int[] {
                0, 0, 136331265, 136340480, 136347689, 136355870,
                136364603, 136372302, 0, 0, 136398858, 0, 0,
                136423465, 0, 0, 136445968, 136456205, 0, 0,
                0, 0, 0, 0
        }, character.partIffIds());
        assertArrayEquals(new int[] {
                0, 0, 7000, 0, 7008, 7007, 7006, 7006, 0, 0, 7005, 0, 0, 7001, 0, 0, 7002, 7003, 0, 0, 0, 0, 0, 0
        }, character.partUids());
        assertArrayEquals(new int[] {8, 5, 4, 2, 1}, character.stats());
        assertEquals(0, character.masteryPoints());
        assertArrayEquals(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, character.cardIffIds());
        assertEquals(0, buffer.readableBytes());
    }

    @Test
    @DisplayName("Should correctly encode a pangya character to a binary stream")
    public void test_encrypt() {
        var pangCharacter = new Character(
                262513, 67108872,
                2,
                new int[] {
                        0, 0, 136331362, 136340480, 136347689, 0, 136364603, 136372302, 0, 0, 136398858, 0, 0, 136423465, 0, 0, 136445968, 136456205, 0, 0, 0, 0, 0, 0
                },
                new int[] {
                        0, 0, 7009, 0, 7008, 0, 7006, 7006, 0, 0, 7005, 0, 0, 7001, 0, 0, 7002, 7003, 0, 0, 0, 0, 0, 0
                },
                new int[] {8, 5, 4, 2, 1},
                0,
                new int[10]
        );
        var buffer = Unpooled.buffer();

        pangCharacter.encode(buffer);

        assertEquals(513, buffer.readableBytes());
    }
}
