package com.hikvision.rensu;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//指定自己的工厂类
public class EnterpriseQualityApplication implements CommandLineRunner {

    static private final Logger logger = LoggerFactory.getLogger(EnterpriseQualityApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseQualityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Here we put some pre start.");
    }
}
