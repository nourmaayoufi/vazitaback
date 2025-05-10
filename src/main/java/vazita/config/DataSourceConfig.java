package vazita.config;


import vazita.entity.Center;
import vazita.repository.CenterRepository;
import vazita.util.CenterRoutingDataSource;
import vazita.util.DataSourceContextHolder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class DataSourceConfig {

    private final CenterRepository centerRepository;

    @Value("${spring.datasource.central.url}")
    private String centralUrl;

    @Value("${spring.datasource.central.username}")
    private String centralUsername;

    @Value("${spring.datasource.central.password}")
    private String centralPassword;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    private final Map<Object, Object> dataSources = new ConcurrentHashMap<>();

    @Bean
    @Qualifier("centralDataSource")
    public DataSource centralDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(centralUrl);
        config.setUsername(centralUsername);
        config.setPassword(centralPassword);
        config.setConnectionTimeout(connectionTimeout);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setPoolName("centralPool");
        return new HikariDataSource(config);
    }

    @PostConstruct
    public void initializeLocalDataSources() {
        // Set central context to allow Spring Boot initialization using the central DB
        DataSourceContextHolder.setCentralContext();

        try {
            List<Center> centers = centerRepository.findAll();
            dataSources.put(DataSourceContextHolder.CENTRAL_DATASOURCE, centralDataSource());

            for (Center center : centers) {
                String url = String.format("jdbc:oracle:thin:@%s:%s", center.getMachine(), center.getSid());

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(center.getUsername());
                config.setPassword(center.getPassword());
                config.setConnectionTimeout(connectionTimeout);
                config.setMaximumPoolSize(maximumPoolSize);
                config.setMinimumIdle(minimumIdle);
                config.setPoolName("centerPool-" + center.getIdCentre());

                dataSources.put(center.getIdCentre(), new HikariDataSource(config));
            }
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    @Bean
    @Primary
    @DependsOn("centralDataSource")
    public DataSource routingDataSource() {
        CenterRoutingDataSource routingDataSource = new CenterRoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(centralDataSource());
        routingDataSource.afterPropertiesSet(); // necessary to initialize routing
        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(routingDataSource());
        em.setPackagesToScan("vazita.model.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
