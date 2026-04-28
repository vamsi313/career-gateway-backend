package com.example.careergateway.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Database configuration that handles Render's DATABASE_URL format.
 * Render provides postgres:// URLs which must be converted to jdbc:postgresql:// for JDBC.
 * Also configures HikariCP to not crash if the database is still initializing (free tier).
 */
@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            logger.info("Configuring datasource from DATABASE_URL environment variable");
            try {
                URI dbUri = new URI(databaseUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                int port = dbUri.getPort();
                if (port == -1) {
                    port = 5432; // Default PostgreSQL port
                }
                String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath() + "?sslmode=require";

                logger.info("JDBC URL: {}", jdbcUrl);
                logger.info("DB Username: {}", username);

                HikariConfig config = new HikariConfig();
                config.setDriverClassName("org.postgresql.Driver");
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setMaximumPoolSize(5);
                config.setMinimumIdle(1);
                config.setConnectionTimeout(60000);
                // Critical: Don't crash on startup if DB is still waking up
                config.setInitializationFailTimeout(-1);
                return new HikariDataSource(config);
            } catch (URISyntaxException e) {
                logger.error("Invalid DATABASE_URL format: {}", databaseUrl, e);
                throw new RuntimeException("Invalid DATABASE_URL", e);
            }
        }

        // Fallback for local development
        logger.info("No DATABASE_URL found, using local development defaults");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/gateway_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("root123");
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }
}
