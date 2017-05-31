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

    @Autowired
    JestClient jestClient;

    static private final Logger logger = LoggerFactory.getLogger(EnterpriseQualityApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseQualityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Here we put some pre start.");

        SearchEntry entry = new SearchEntry();
        entry.setId("2");
        entry.setCaseName("综合安防");
        entry.setDescription("Rex test 为了综合安防");
        entry.setTitle("iVMS8200");
        entry.setRemark("保留字段");


        Index.Builder indexEntryBuilder = new Index.Builder(entry).index("doc_index").type("doc").refresh(true);

        /**
         * .refresh(true)
         * indexEntryBuilder.refresh(true);
         * .setParameter(Parameters.REFRESH, true)
        */
        jestClient.execute(indexEntryBuilder.build());

    }
}
