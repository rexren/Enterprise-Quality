package com.hikvision.rensu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.hikvision.rensu.cert.repository.base.HikBasicRepositoryFactoryBean;

@SpringBootApplication
@EnableWebSecurity //启用web安全  
@EnableJpaRepositories(repositoryFactoryBeanClass = HikBasicRepositoryFactoryBean.class)
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
