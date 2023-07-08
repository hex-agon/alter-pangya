package work.fking.pangya.resources

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.apache.logging.log4j.LogManager
import java.io.IOException

private val LOGGER = LogManager.getLogger(ResourceLoader::class.java)

class ResourceLoader {

    private val cachedResources: MutableMap<String, ByteBuf?> = HashMap()

    fun load(resource: String): ByteBuf? {
        return cachedResources.computeIfAbsent(resource) { loadResource(resource) }
    }

    private fun loadResource(resource: String): ByteBuf? {
        return try {
            val bytes = ResourceLoader::class.java.getResourceAsStream(resource)?.readAllBytes()
            Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(bytes))
        } catch (e: IOException) {
            LOGGER.error("Failed to load resource", e)
            null
        }
    }
}
