package com.daim.blog.controller.rest;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.infrastructure.json.Json;
import com.daim.blog.model.UserRegisterModel;
import com.daim.blog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BlogBackendApplication.class)
@AutoConfigureMockMvc
public class UserControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void register() throws Exception {
        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setEmail("email@email.com");
        userRegisterModel.setUsername("user");
        userRegisterModel.setName("name");
        userRegisterModel.setPassword("password");
        userRegisterModel.setUrlPath("url");

        mvc.perform(post("/service/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.getInstance().getObjectMapper().writeValueAsBytes(userRegisterModel)))
                .andExpect(status().isOk());

        UserEntity userEntity = repository.findByUsername("user");
        assertEquals("email@email.com", userEntity.getEmail());
        assertEquals("user", userEntity.getUsername());
        assertEquals("name", userEntity.getName());
        assertEquals("url", userEntity.getUrlPath());

        mvc.perform(post("/service/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.getInstance().getObjectMapper().writeValueAsBytes(userRegisterModel)))
                .andExpect(status().isBadRequest()).andExpect(content().json("{\"message\":\"Username is already taken\"}"));

        repository.deleteAll();
    }
}