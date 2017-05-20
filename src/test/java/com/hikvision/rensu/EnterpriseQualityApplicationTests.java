package com.hikvision.rensu;

import com.hikvision.rensu.cert.domain.Post;
import com.hikvision.rensu.cert.domain.Tag;
import com.hikvision.rensu.cert.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnterpriseQualityApplicationTests {

    @Autowired
    private PostService postService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before() {
        elasticsearchTemplate.deleteIndex(Post.class);
        elasticsearchTemplate.createIndex(Post.class);
        elasticsearchTemplate.putMapping(Post.class);
        elasticsearchTemplate.refresh(Post.class);
    }

    @Test
    public void testSave() throws Exception {
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
        postService.save(post);

        assertNotNull(post.getId());

        Post post2 = new Post();
        post2.setId("2");
        post2.setTitle("Bigining with spring boot application");
        post2.setTags(Arrays.asList(tag));
        post2.setRating(7.5);
        postService.save(post2);
        assertNotNull(post2.getId());
    }

    @Test //tag必须不是nested的
    public void testFindByTagsName() throws Exception {
        Tag tag = new Tag();
        tag.setId("1");
        tag.setName("tech");
        Tag tag2 = new Tag();
        tag2.setId("2");
        tag2.setName("elasticsearch");

        Post post = new Post();
        post.setId("1");
        post.setTitle("Bigining with spring boot application and elasticsearch");
        post.setRating(9.4);
        post.setTags(Arrays.asList(tag, tag2));
        postService.save(post);


        Post post2 = new Post();
        post2.setId("1");
        post2.setTitle("Bigining with spring boot application");
        post2.setTags(Arrays.asList(tag));
        post2.setRating(9.6);
        postService.save(post2);

        Page<Post> posts = postService.findByTagsName("tech", new PageRequest(0, 10));
        Page<Post> posts2 = postService.findByTagsName("tech", new PageRequest(0, 10));
        Page<Post> posts3 = postService.findByTagsName("maz", new PageRequest(0, 10));


        assertEquals(posts.getTotalElements(), 1L);
        assertEquals(posts2.getTotalElements(), 1L);
        assertEquals(posts3.getTotalElements(), 0L);
    }
}
