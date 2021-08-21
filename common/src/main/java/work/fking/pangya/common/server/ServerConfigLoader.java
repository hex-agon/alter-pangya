package work.fking.pangya.common.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ServerConfigLoader {

    private static final ObjectMapper MAPPER = new TomlMapper();

    private ServerConfigLoader() {
    }

    public static ServerConfig load(String path) throws IOException {
        var configPath = Path.of(path);
        return MAPPER.readValue(Files.newInputStream(configPath), ServerConfig.class);
    }
}
