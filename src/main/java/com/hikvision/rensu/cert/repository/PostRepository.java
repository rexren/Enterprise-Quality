package com.hikvision.rensu.cert.repository;

import com.hikvision.rensu.cert.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by rensu on 2017/5/12.
 */
public interface PostRepository extends ElasticsearchRepository<Post, String> {

    Page<Post> findByTagsName(String name, Pageable pageable);

    List<Post> findByRatingBetween(Double beginning, Double end);

}
