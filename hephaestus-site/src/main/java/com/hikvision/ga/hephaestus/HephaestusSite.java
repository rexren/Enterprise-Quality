package com.hikvision.ga.hephaestus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主类
 *
 */
@SpringBootApplication(scanBasePackages = { "com.hikvision.ga.common.boot", "com.hikvision.ga.hephaestus"})
@EnableTransactionManagement // 启动事务管理
@EnableWebSecurity // 启用web安全
public class HephaestusSite implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(HephaestusSite.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    // TODO Auto-generated method stub
  }

}
