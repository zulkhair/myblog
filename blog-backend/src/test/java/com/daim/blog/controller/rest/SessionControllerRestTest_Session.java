package com.daim.blog.controller.rest;

import antlr.Token;
import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.infrastructure.json.Json;
import com.daim.blog.infrastructure.jwt.TokenManager;
import com.daim.blog.model.JwtRequestModel;
import com.daim.blog.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BlogBackendApplication.class)
@AutoConfigureMockMvc
public class SessionControllerRestTest_Session {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenManager tokenManager;

    @Test
    public void checkSession() throws Exception {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userRepository.save(userEntity);

        UserDetails user = new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
        String token = tokenManager.generateJwtToken(user);

        //Execute & Assert
        mvc.perform(get("/service/session").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
        userRepository.deleteAll();


        mvc.perform(get("/service/session").header("Authorization", "Bearer " + token)).andExpect(status().isUnauthorized());
    }
}