package work.fking.pangya.login.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DatabaseModule extends AbstractModule {

    private DatabaseModule() {
    }

    public static DatabaseModule create() {
        return new DatabaseModule();
    }

    @Override
    protected void configure() {
        Map<String, String> properties = new HashMap<>();

        properties.put("databaseHost", System.getenv("DATABASE_HOST"));
        properties.put("databaseName", System.getenv("DATABASE_NAME"));
        properties.put("databaseUsername", System.getenv("DATABASE_USERNAME"));
        properties.put("databasePassword", System.getenv("DATABASE_PASSWORD"));

        Names.bindProperties(binder(), properties);
    }

    @Provides
    @Singleton
    public DataSource provideHikariDataSource(@Named("databaseHost") String host, @Named("databaseName") String database, @Named("databaseUsername") String username, @Named("databasePassword") String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource, @Named("shared") ObjectMapper objectMapper) {
        Jdbi jdbi = Jdbi.create(dataSource)
                        .installPlugin(new PostgresPlugin())
                        .installPlugin(new SqlObjectPlugin())
                        .installPlugin(new Jackson2Plugin());

        jdbi.getConfig(Jackson2Config.class).setMapper(objectMapper);
        return jdbi;
    }
}
