package com.hikvision.rensu.cert.search;

import io.searchbox.client.JestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Using Thread pool to schedule index items.
 * Created by rensu on 2017/5/31.
 */
@Service
public class SchedulerIndexService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static final Log logger = LogFactory.getLog(SchedulerIndexService.class);

    @SuppressWarnings("unused")
	@Autowired
    private JestClient jestClient;

    /**
     * we use this method to index a item
     * @param indexer
     * @param <T>
     * TODO: 这里最好改为 bulk 方式来index
     */
    public <T> void index(final Indexer<T> indexer) {
        logger.debug("Indexing " + indexer.name());
        for (final T indexable : indexer.indexableItems()) {
            executorService.submit(() -> {
                try {
                    indexer.indexItem(indexable);
                } catch (Exception e) {
                    String message =
                            String.format("Unable to index an entry of '%s' with id: '%s' -> (%s, %s)", indexer
                                    .name(), indexer.getId(indexable), e.getClass().getName(), e
                                    .getMessage());

                    logger.warn(message);
                }
            });
        }

    }

}
