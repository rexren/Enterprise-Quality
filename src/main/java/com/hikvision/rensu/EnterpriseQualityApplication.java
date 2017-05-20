package com.hikvision.rensu;

import com.hikvision.rensu.cert.domain.Post;
import com.hikvision.rensu.cert.domain.Tag;
import com.hikvision.rensu.cert.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class EnterpriseQualityApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseQualityApplication.class, args);
    }

    @Autowired
    private PostService service;


    @Override
    public void run(String... args) throws Exception {
        Tag tag = new Tag();
        tag.setId("1");
        tag.setName("tech");
        Tag tag2 = new Tag();
        tag2.setId("2");
        tag2.setName("elasticsearch");

        Post post = new Post();
        post.setId("1");
        post.setTitle("Bigining with spring boot application and elasticsearch");
        post.setRating(9.5);
        post.setTags(Arrays.asList(tag, tag2));

        service.save(post);


        Post post2 = new Post();
        post2.setId("2");
        post2.setTitle("Bigining with spring boot application");
        post2.setTags(Arrays.asList(tag));
        post2.setRating(7.5);
        service.save(post2);
    }
}
