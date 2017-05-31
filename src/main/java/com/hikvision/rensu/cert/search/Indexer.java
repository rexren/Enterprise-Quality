package com.hikvision.rensu.cert.search;

/**
 * Created by rensu on 2017/5/31.
 */
public interface Indexer<T> {

    /**
     * get a list of items need for index
     * @return
     */
    public Iterable<T> indexableItems();

    /**
     * The index name
     * @return
     */
    public String name();

    /**
     * index this item
     * @param indexable
     */
    public void indexItem(T indexable);


    /**
     * get index id
     * @param indexable
     * @return
     */
    public String getId(T indexable);
}