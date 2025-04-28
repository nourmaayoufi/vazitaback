package vazita.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceRouter router = new DataSourceRouter();
        
        Map<Object, Object> dataSources = new HashMap<>();
        // Add the central database as the default
        dataSources.put("CENTRALE_MOBILE", centralDataSource());
        
        // In a real scenario, you would load all center databases from CENTRE_CVT table
        // For Sprint 1, we're just setting up the infrastructure
        
        router.setTargetDataSources(dataSources);
        router.setDefaultTargetDataSource(centralDataSource());
        
        return router;
    }
    
    @Bean
    @Qualifier("centralDataSource")
    public DataSource centralDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("spring.datasource.central.url"));
        config.setUsername(env.getProperty("spring.datasource.central.username"));
        config.setPassword(env.getProperty("spring.datasource.central.password"));
        config.setDriverClassName(env.getProperty("spring.datasource.central.driver-class-name"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setPoolName("CentralHikariPool");
        
        return new HikariDataSource(config);
    }
    
    // Method to create a center-specific datasource
    public DataSource createCenterDataSource(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setPoolName("Center-HikariPool");
        
        return new HikariDataSource(config);
    }
}