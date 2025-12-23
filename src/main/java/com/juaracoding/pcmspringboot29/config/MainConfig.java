package com.juaracoding.pcmspringboot29.config;

import com.juaracoding.pcmspringboot29.security.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Random;

@Configuration
public class MainConfig {

    @Autowired
    private Environment env;

    @Bean
    public Random getRandom(){
        return new Random();
    }

    @Primary
    @Bean
    public DataSource getDataSource(){
        DataSourceBuilder dBuild = DataSourceBuilder.create();
        dBuild.driverClassName(env.getProperty("spring.datasource.driverClassName"));
        dBuild.password(Crypto.performDecrypt(env.getProperty("spring.datasource.password")));
        dBuild.username(Crypto.performDecrypt(env.getProperty("spring.datasource.username")));
        dBuild.url(Crypto.performDecrypt(env.getProperty("spring.datasource.url")));
        return dBuild.build();
    }
}