package org.andrea.jockey.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.andrea.jockey")
@PropertySource("classpath:database.properties")
public class AppConfig {

    @Bean
    DataSource getDataSource(){

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/jockey");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("p0o9i8u7");
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return driverManagerDataSource;

    }


}
