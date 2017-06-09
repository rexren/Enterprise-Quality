package com.hikvision.rensu.cert.search;

import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Using this class to search content
 * Created by rensu on 2017/6/4.
 */
@Service
public class SearchService {

    @Autowired
    private static Log logger = LogFactory.getLog(SearchService.class);

    @Autowired
    private JestClient jestClient;


    public SearchResult search(String term, Pageable pageable) {
        Search.Builder searchBuilder = new Search.Builder("");

        Search search = searchBuilder.build();
        //logger.debug(search.getData(this.gson));
        JestResult jestResult = execute(search);
        return null;
    }

    private JestResult execute(Action action) {
        try {
            JestResult result = jestClient.execute(action);
            logger.debug(result.getJsonString());
            if (!result.isSucceeded()) {
                logger.warn("Failed to execute Elastic Search action: " + result.getErrorMessage()
                        + " " + result.getJsonString());
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
