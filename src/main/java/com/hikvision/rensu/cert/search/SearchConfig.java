package com.hikvision.rensu.cert.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rensu on 2017/5/30.
 */
@Configuration
public class SearchConfig {

    private static Logger logger = LoggerFactory.getLogger(SearchConfig.class);

    @Bean
    public Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
    }

    @Bean
    public JestClient jestClient() {
        String connectionUri = "http://localhost:9200";
        logger.info("**** Elastic Search endpoint: " + connectionUri);

        HttpClientConfig clientConfig = new HttpClientConfig
                .Builder(connectionUri)
                .multiThreaded(true)
                .gson(gson())
                .build();

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        return factory.getObject();
    }

}
