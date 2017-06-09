package com.hikvision.rensu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by rensu on 2017/6/5.
 */
public class SearchTest {

    @Test
    public void f(){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.boolQuery().
        //        must(QueryBuilders.matchQuery("title", "Search")).must(QueryBuilders.matchQuery("content", "Elasticsearch")).filter(QueryBuilders.rangeQuery("").gt(1)));

        searchSourceBuilder.query(QueryBuilders.matchQuery("fds", "fds"));
        System.out.println(searchSourceBuilder.toString());
    }

    public static void main(String[] args) {
        JestClient jestClient = new JestConf().jestClient();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("user", "kimchy"));
        searchSourceBuilder.query(QueryBuilders.boolQuery().
                must(QueryBuilders.matchQuery("title", "Search")).must(QueryBuilders.matchQuery("content", "Elasticsearch")).filter(QueryBuilders.rangeQuery("").gt(1)));



        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex("doc")
                .addType("inspection")
                .build();



        try {
            JestResult jestResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


class JestConf {
    private String elasticSearchUrl = "http://localhost:9200";

    public Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
    }

    public JestClient jestClient() {
        String connectionUri = elasticSearchUrl;

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