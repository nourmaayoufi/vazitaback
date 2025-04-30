package vazita.config;


import vazita.util.CenterContextHolder;
import vazita.util.DataSourceRouter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    /**
     * Create the central data source for authentication and user management
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.centrale")
    public HikariConfig centralHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource centralDataSource() {
        return new HikariDataSource(centralHikariConfig());
    }
    
    /**
     * Create the routing data source that switches between different center databases
     */
    @Bean
    @Primary
    public DataSource routingDataSource() {
        AbstractRoutingDataSource routingDataSource = new DataSourceRouter();
        
        // Set the central data source as default
        routingDataSource.setDefaultTargetDataSource(centralDataSource());
        
        // Initialize with central data source, centers will be added dynamically
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("CENTRALE", centralDataSource());
        
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.afterPropertiesSet();
        
        return routingDataSource;
    }
}