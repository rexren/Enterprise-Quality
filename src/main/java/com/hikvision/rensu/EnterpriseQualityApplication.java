package com.hikvision.rensu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //启动事务管理
@EnableWebSecurity //启用web安全  
public class EnterpriseQualityApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseQualityApplication.class, args);
    }

}
