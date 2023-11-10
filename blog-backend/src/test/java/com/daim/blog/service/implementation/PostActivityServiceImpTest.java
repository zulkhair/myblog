package com.daim.blog.service.implementation;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.CommentEntity;
import com.daim.blog.entity.CounterEntity;
import com.daim.blog.entity.PostEntity;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.entity.id.CounterId;
import com.daim.blog.model.*;
import com.daim.blog.repository.CommentRepository;
import com.daim.blog.repository.CounterRepository;
import com.daim.blog.repository.PostRepository;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.PostActivityService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BlogBackendApplication.class})
@AutoConfigureMockMvc
public class PostActivityServiceImpTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostActivityService postActivityService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Test
    public void viewPost() {
        // Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        //Execution
        List<PostResponseModel> postResponseModels = postActivityService.viewPost("username");

        //Assertion
        assertSame(1, postResponseModels.size());
        assertEquals("name", postResponseModels.get(0).getAuthor());
        assertEquals("Title", postResponseModels.get(0).getTitle());
        assertEquals("Content", postResponseModels.get(0).getContent());
        assertEquals("name", postResponseModels.get(0).getAuthor());

        //Execution
        postResponseModels = postActivityService.viewPost();
        assertSame(0L, postResponseModels.get(0).getViewCount());

        postActivityService.viewDetailPost(postEntity.getId(), "ip1", null);
        postResponseModels = postActivityService.viewPost();

        //Assertion
        assertSame(1, postResponseModels.size());
        assertEquals("name", postResponseModels.get(0).getAuthor());
        assertEquals("Title", postResponseModels.get(0).getTitle());
        assertEquals("Content", postResponseModels.get(0).getContent());
        assertEquals("name", postResponseModels.get(0).getAuthor());
        assertSame(1L, postResponseModels.get(0).getViewCount());

        //Cleanup
        counterRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void viewPostByUrlPath() {
        // Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        //Execution
        List<PostResponseModel> postResponseModels = postActivityService.viewPostByUrlPath("url");

        //Assertion
        assertSame(1, postResponseModels.size());
        assertEquals("name", postResponseModels.get(0).getAuthor());
        assertEquals("Title", postResponseModels.get(0).getTitle());
        assertEquals("Content", postResponseModels.get(0).getContent());
        assertEquals("name", postResponseModels.get(0).getAuthor());

        //Cleanup
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createPost() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        CreatePostModel createPostModel = new CreatePostModel();
        createPostModel.setContent("Content");
        createPostModel.setTitle("Title");

        //Execution
        postActivityService.createPost(createPostModel, userEntity.getUsername());

        //Assertion
        List<PostEntity> postEntities = postRepository.findAllByOrderByPostTimeDesc();
        assertSame(1, postEntities.size());
        assertEquals("name", postEntities.get(0).getUser().getName());
        assertEquals("Title", postEntities.get(0).getTitle());
        assertEquals("Content", new String(postEntities.get(0).getContent()));

        //Cleanup
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void editPost() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        EditPostModel editPostModel = new EditPostModel();
        editPostModel.setPostId(postEntity.getId());
        editPostModel.setContent("ContentEdit");
        editPostModel.setTitle("TitleEdit");

        //Execution
        postActivityService.editPost(editPostModel, "username");

        //Assertion
        PostEntity postEntityEdit = postRepository.findById(postEntity.getId()).get();
        assertEquals("ContentEdit", new String(postEntityEdit.getContent()));
        assertEquals("TitleEdit", postEntityEdit.getTitle());

        assertThrows(NoSuchElementException.class, () -> {
            postActivityService.editPost(editPostModel, "null");
        });

        //Clean up
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void deletePost() throws SQLException {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        //Execution
        postActivityService.deletePost(postEntity.getId(), "username");

        //Asertion
        List<PostEntity> postEntities = (List<PostEntity>) postRepository.findAll();
        assertSame(0, postEntities.size());

        assertThrows(NoSuchElementException.class, () -> {
            postActivityService.deletePost(postEntity.getId(), "null");
        });

        //clean up
        userRepository.deleteAll();
    }

    @Test
    public void viewDetailPost() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        CommentEntity commentEntity = new CommentEntity("Comment", "CommentName", postEntity);
        commentRepository.save(commentEntity);

        //Execution and Assertion
        PostResponseModel postResponseModel = postActivityService.viewDetailPost(postEntity.getId(), "ip1", "username");

        assertEquals("name", postResponseModel.getAuthor());
        assertEquals("Content", postResponseModel.getContent());
        assertEquals("Title", postResponseModel.getTitle());
        assertSame(1, postResponseModel.getComments().size());
        assertEquals(postEntity.getPostTime(), postResponseModel.getPostTime());
        assertSame(1L, postResponseModel.getViewCount());
        assertSame(true, postResponseModel.getDeleteComment());

        PostResponseModel postResponseModel2 = postActivityService.viewDetailPost(postEntity.getId(), "ip2", "asd");
        assertSame(2L, postResponseModel2.getViewCount());

        CounterEntity counterEntity = counterRepository.findById(new CounterId(postEntity.getId(), "ip1")).get();
        DateTime dateTime = DateTime.now().minusDays(1);
        counterEntity.setLastUpdatedTime(dateTime.toDate());
        counterRepository.save(counterEntity);

        PostResponseModel postResponseModel3 = postActivityService.viewDetailPost(postEntity.getId(), "ip1", null);
        assertSame(3L, postResponseModel3.getViewCount());

        //Clean up
        counterRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void createComment() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        CreateCommentModel createCommentModel = new CreateCommentModel();
        createCommentModel.setComment("Comment");
        createCommentModel.setPostId(postEntity.getId());
        createCommentModel.setName("name");

        //Execution
        postActivityService.createComment(createCommentModel);

        //Assertion
        List<CommentEntity> commentEntities = (List<CommentEntity>) commentRepository.findAll();
        assertSame(1, commentEntities.size());
        assertEquals("name", commentEntities.get(0).getName());
        assertEquals("Comment", commentEntities.get(0).getContent());

        //Clean up
        commentRepository.deleteAll();
        counterRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void deleteComment() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        PostEntity postEntity = new PostEntity("Title", "Content".getBytes(), userEntity);
        postRepository.save(postEntity);

        CommentEntity commentEntity = new CommentEntity("Comment", "CommentName", postEntity);
        commentRepository.save(commentEntity);

        //Execution
        postActivityService.deleteComment(commentEntity.getId(), "username");

        //Assertion
        List<CommentEntity> commentEntities = (List<CommentEntity>) commentRepository.findAll();
        assertSame(0, commentEntities.size());

        assertThrows(NoSuchElementException.class, () -> {
            postActivityService.deleteComment(commentEntity.getId(), "null");
        });

        //Clean up
        postRepository.deleteAll();
        userRepository.deleteAll();

    }


}