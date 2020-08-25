package com.github.espress91.kakaopaysprinkling.configure;

import com.github.espress91.kakaopaysprinkling.util.MessageUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.modelmapper.ModelMapper;

import javax.sql.DataSource;

@Configuration
public class ServiceConfigure {

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_kakaopay_sprinkling;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        HikariDataSource dataSource = (HikariDataSource) factory.build();
        dataSource.setPoolName("TEST_H2_DB");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
        return new Log4jdbcProxyDataSource(dataSource);
    }

    @Bean
    public MessageUtils messageUtils() {
        return MessageUtils.getInstance();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
