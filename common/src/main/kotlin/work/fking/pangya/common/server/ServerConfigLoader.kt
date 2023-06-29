package work.fking.pangya.common.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Path

object ServerConfigLoader {
    private val MAPPER: ObjectMapper = TomlMapper().registerKotlinModule()

    fun load(path: String): ServerConfig {
        val configPath = Path.of(path)
        return MAPPER.readValue<ServerConfig>(Files.newInputStream(configPath))
    }
}
