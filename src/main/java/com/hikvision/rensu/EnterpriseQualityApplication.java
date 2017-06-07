package com.hikvision.rensu;

import com.hikvision.rensu.cert.search.SearchEntry;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.params.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
