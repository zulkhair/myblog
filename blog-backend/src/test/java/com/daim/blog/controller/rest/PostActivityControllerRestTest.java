package com.daim.blog.controller.rest;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.CommentEntity;
import com.daim.blog.entity.PostEntity;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.infrastructure.json.Json;
import com.daim.blog.infrastructure.jwt.TokenManager;
import com.daim.blog.model.*;
import com.daim.blog.repository.CommentRepository;
import com.daim.blog.repository.PostRepository;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.PostActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BlogBackendApplication.class})
@AutoConfigureMockMvc
@PropertySource("classpath:application-test.properties")
public class PostActivityControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PostActivityService postActivityService;

    @Test
    public void viewPost() throws Exception {
        // Preparation
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UserEntity userEntity = new UserEntity("username" + i, "name" + i, "email" + i, "url" + i);
            userEntity.setPassword(passwordEncoder.encode("password" + i));
            userRepository.save(userEntity);

            userEntities.add(userEntity);
        }

        Date now = new Date();
        String formatedDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        CommentResponseModel commentResponseModel = new CommentResponseModel();
        commentResponseModel.setContent("Comment");
        commentResponseModel.setPostTime(now);
        commentResponseModel.setName("name");

        PostResponseModel postResponseModel = new PostResponseModel();
        postResponseModel.setPostTime(now);
        postResponseModel.setAuthor("name");
        postResponseModel.setUpdateTime(now);
        postResponseModel.setComments(new ArrayList<>());
        postResponseModel.getComments().add(commentResponseModel);
        postResponseModel.setTitle("Title");
        postResponseModel.setContent("Content");

        List<PostResponseModel> postResponseModels = new ArrayList<>();
        postResponseModels.add(postResponseModel);


        Mockito.when(postActivityService.viewPost()).thenReturn(postResponseModels);
        Mockito.when(postActivityService.viewPostByUrlPath(Mockito.any())).thenReturn(postResponseModels);

        // Exec and Assert
        mvc.perform(get("/service/post/view"))
                .andExpect(content().json("{\"data\":[{\"title\":\"Title\",\"content\":\"Content\",\"postTime\":\"" + formatedDateNow + "\",\"updateTime\":\"" + formatedDateNow + "\",\"author\":\"name\",\"comments\":[{\"content\":\"Comment\",\"postTime\":\"" + formatedDateNow + "\",\"name\":\"name\"}]}]}"));

        mvc.perform(get("/service/post/view").param("urlPath", "url1"))
                .andExpect(content().json("{\"data\":[{\"title\":\"Title\",\"content\":\"Content\",\"postTime\":\"" + formatedDateNow + "\",\"updateTime\":\"" + formatedDateNow + "\",\"author\":\"name\",\"comments\":[{\"content\":\"Comment\",\"postTime\":\"" + formatedDateNow + "\",\"name\":\"name\"}]}]}"));


        //Clean up
        userRepository.deleteAll();
    }

    @Test
    public void viewPostAdmin() throws Exception {
        // Preparation
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UserEntity userEntity = new UserEntity("username" + i, "name" + i, "email" + i, "url" + i);
            userEntity.setPassword(passwordEncoder.encode("password" + i));
            userRepository.save(userEntity);

            userEntities.add(userEntity);
        }

        UserDetails user = new User(userEntities.get(0).getUsername(), userEntities.get(0).getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        Date now = new Date();
        String formatedDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        CommentResponseModel commentResponseModel = new CommentResponseModel();
        commentResponseModel.setContent("Comment");
        commentResponseModel.setPostTime(now);
        commentResponseModel.setName("name");

        PostResponseModel postResponseModel = new PostResponseModel();
        postResponseModel.setPostTime(now);
        postResponseModel.setAuthor("name");
        postResponseModel.setUpdateTime(now);
        postResponseModel.setComments(new ArrayList<>());
        postResponseModel.getComments().add(commentResponseModel);
        postResponseModel.setTitle("Title");
        postResponseModel.setContent("Content");

        List<PostResponseModel> postResponseModels = new ArrayList<>();
        postResponseModels.add(postResponseModel);

        Mockito.when(postActivityService.viewPost(Mockito.any())).thenReturn(postResponseModels);

        // Exec and Assert
        mvc.perform(get("/service/post/admin").header("Authorization", "Bearer " + token))
                .andExpect(content().json("{\"data\":[{\"title\":\"Title\",\"content\":\"Content\",\"postTime\":\"" + formatedDateNow + "\",\"updateTime\":\"" + formatedDateNow + "\",\"author\":\"name\",\"comments\":[{\"content\":\"Comment\",\"postTime\":\"" + formatedDateNow + "\",\"name\":\"name\"}]}]}"));

        //Clean up
        userRepository.deleteAll();
    }

    @Test
    public void createPost() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        CreatePostModel createPostModel = new CreatePostModel();
        createPostModel.setTitle("Title");
        createPostModel.setContent("Content");

        Mockito.doNothing().when(postActivityService).createPost(Mockito.any(), Mockito.any());

        //Execution
        mvc.perform(post("/service/post/create").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsString(createPostModel)))
                .andExpect(status().isOk());

        //Clean up
        userRepository.deleteAll();
    }

    @Test
    public void editPost() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        EditPostModel editPostModel = new EditPostModel();
        editPostModel.setPostId("id");
        editPostModel.setContent("ContentEdit");
        editPostModel.setTitle("TitleEdit");

        //Execution and assertion
        Mockito.doNothing().when(postActivityService).editPost(Mockito.any(), Mockito.any());
        mvc.perform(post("/service/post/edit").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsString(editPostModel)))
                .andExpect(status().isOk());

        Mockito.doThrow(new NoSuchElementException()).when(postActivityService).editPost(Mockito.any(), Mockito.any());
        mvc.perform(post("/service/post/edit").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsString(editPostModel)))
                .andExpect(status().is4xxClientError());

        //Clean up
        userRepository.deleteAll();
    }

    @Test
    public void deletePost() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        //Execution and assertion
        Mockito.doNothing().when(postActivityService).deletePost(Mockito.any(), Mockito.any());
        mvc.perform(delete("/service/post/delete/111").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Mockito.doThrow(new NoSuchElementException()).when(postActivityService).deletePost(Mockito.any(), Mockito.any());
        mvc.perform(delete("/service/post/delete/123").header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());

        Mockito.doThrow(new SQLException()).when(postActivityService).deletePost(Mockito.any(), Mockito.any());
        mvc.perform(delete("/service/post/delete/222").header("Authorization", "Bearer " + token))
                .andExpect(status().is5xxServerError());

        //clean up
        userRepository.deleteAll();
    }

    @Test
    public void viewDetailPost() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        Date now = new Date();
        String formatedDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        CommentResponseModel commentResponseModel = new CommentResponseModel();
        commentResponseModel.setContent("Comment");
        commentResponseModel.setPostTime(now);
        commentResponseModel.setName("name");

        PostResponseModel postResponseModel = new PostResponseModel();
        postResponseModel.setPostTime(now);
        postResponseModel.setAuthor("name");
        postResponseModel.setUpdateTime(now);
        postResponseModel.setComments(new ArrayList<>());
        postResponseModel.getComments().add(commentResponseModel);
        postResponseModel.setTitle("Title");
        postResponseModel.setContent("Content");
        Mockito.when(postActivityService.viewDetailPost(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(postResponseModel);

        mvc.perform(get("/service/post/view/123"))
                .andExpect(content().json("{\"data\":{\"title\":\"Title\",\"content\":\"Content\",\"postTime\":\""+formatedDateNow+"\",\"updateTime\":\""+formatedDateNow+"\",\"author\":\"name\",\"comments\":[{\"content\":\"Comment\",\"postTime\":\""+formatedDateNow+"\",\"name\":\"name\"}]}}"));

        mvc.perform(get("/service/post/view/123").header("Authorization", "Bearer " + token))
                .andExpect(content().json("{\"data\":{\"title\":\"Title\",\"content\":\"Content\",\"postTime\":\""+formatedDateNow+"\",\"updateTime\":\""+formatedDateNow+"\",\"author\":\"name\",\"comments\":[{\"content\":\"Comment\",\"postTime\":\""+formatedDateNow+"\",\"name\":\"name\"}]}}"));

        Mockito.when(postActivityService.viewDetailPost(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new NoSuchElementException());

        mvc.perform(get("/service/post/view/123"))
                .andExpect(status().isBadRequest());

        // Clean up
        userRepository.deleteAll();

    }

    @Test
    public void createComment() throws Exception {
        //Preparation
        Mockito.doNothing().when(postActivityService).createComment(Mockito.any());

        CreateCommentModel createCommentModel = new CreateCommentModel();
        createCommentModel.setComment("Comment");
        createCommentModel.setPostId("1");
        createCommentModel.setName("name");

        //Exec & Assert
        mvc.perform(post("/service/post/comment").contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsString(createCommentModel)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteComment() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        Mockito.doNothing().when(postActivityService).deleteComment(Mockito.any(), Mockito.any());

        mvc.perform(delete("/service/post/comment/delete/commentId").header("Authorization", "Bearer " + token).content("222").content("commentId"))
                .andExpect(status().isOk());
    }
}