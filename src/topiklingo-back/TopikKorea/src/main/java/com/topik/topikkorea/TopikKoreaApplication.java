package com.topik.topikkorea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@PropertySource(value = "classpath:.env")
public class TopikKoreaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopikKoreaApplication.class, args);
    }
}
