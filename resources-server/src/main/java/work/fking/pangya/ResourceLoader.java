package work.fking.pangya;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ResourceLoader {

    private final Map<String, ByteBuf> cachedResources = new HashMap<>();

    public ByteBuf load(String resource) {
        return cachedResources.computeIfAbsent(resource, this::loadResource);
    }

    private ByteBuf loadResource(String resource) {
        try {
            byte[] bytes = ResourceLoader.class.getResourceAsStream(resource)
                                               .readAllBytes();

            return Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(bytes));
        } catch (IOException e) {
            LOGGER.error("Failed to load resource", e);
            return null;
        }
    }
}
