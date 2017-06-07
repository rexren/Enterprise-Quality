package com.hikvision.rensu.cert.search;

/**
 * mapper origin item to index item
 * Created by rensu on 2017/6/1.
 */
public interface IndexMapper<T> {
    <R extends SearchEntry> R map(T item);
}
