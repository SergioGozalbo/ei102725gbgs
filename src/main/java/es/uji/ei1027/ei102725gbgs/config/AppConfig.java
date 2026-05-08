package es.uji.ei1027.ei102725gbgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * Configuration class for the application. It sets up view controllers and configures the DataSource bean.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Configure view controllers to redirect the root URL ("/") to the login page ("/login").
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
    }

    /**
     * Configure the DataSource bean using properties prefixed with "spring.datasource" from application.properties.
     * @return a DataSource object configured with the specified properties.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
