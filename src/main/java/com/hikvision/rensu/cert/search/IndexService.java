package com.hikvision.rensu.cert.search;

import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rensu on 2017/5/31.
 */
@Service
public class IndexService {

    private static final Log logger = LogFactory.getLog(IndexService.class);

    @Autowired
    private JestClient jestClient;

    /**
     * Normally we use this method to index entry
     * @param entry the item need to index
     */
    public void saveToIndex(SearchEntry entry) {
        Index.Builder indexEntryBuilder = new Index.Builder(entry)
                .id(entry.getId())
                .index("doc_index")
                .type(entry.getType())
                .refresh(true);

        execute(indexEntryBuilder.build());
    }

    /**
     * And we use this to index list of entries
     * @param entries the items need to index
     */
    public void saveToIndex(List<SearchEntry> entries){

        String type = entries.get(0).getType();

        List<Index> indexs = entries
                .stream()
                .map(t->new Index.Builder(t).build())
                .collect(Collectors.toList());

        Bulk bulk = new Bulk.Builder()
                .defaultIndex("doc_index")
                .defaultType(type)
                .addAction(indexs)
                .refresh(true)
                .build();

        execute(bulk);
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
            throw new RuntimeException(e);
        }
    }
}
