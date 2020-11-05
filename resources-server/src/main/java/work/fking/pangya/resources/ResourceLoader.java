package work.fking.pangya.resources;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {

    private static final Logger LOGGER = LogManager.getLogger(ResourceLoader.class);

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
