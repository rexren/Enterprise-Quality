package com.hikvision.rensu.cert.search;

/**
 * Created by rensu on 2017/5/31.
 */
public interface Indexer<T> {

    public Iterable<T> indexableItems();

    public String name();

    public void indexItem(T indexable);

    public String getId(T indexable);
}