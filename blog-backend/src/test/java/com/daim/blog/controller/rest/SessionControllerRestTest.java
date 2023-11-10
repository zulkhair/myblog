package com.daim.blog.controller.rest;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.infrastructure.json.Json;
import com.daim.blog.infrastructure.jwt.TokenManager;
import com.daim.blog.model.JwtRequestModel;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.UserService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BlogBackendApplication.class)
@AutoConfigureMockMvc
public class SessionControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TokenManager tokenManager;

    @MockBean
    AuthenticationManager authenticationManager;

    @Test
    public void createToken_success() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        UserEntity userEntity = new UserEntity("user", "name", "email", "url");
        userEntity.setPassword("password");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userEntity);
        Mockito.when(tokenManager.generateJwtToken(Mockito.any())).thenReturn("token");

        JwtRequestModel jwtRequestModel = new JwtRequestModel();
        jwtRequestModel.setUsername("user");
        jwtRequestModel.setPassword("password");

        mvc.perform(post("/service/login").contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsBytes(jwtRequestModel)))
                .andExpect(content().json("{\"data\":{\"token\":\"token\"}}"));


    }

    @Test
    public void createToken_wrongPassword() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException(""));

        JwtRequestModel jwtRequestModel = new JwtRequestModel();
        jwtRequestModel.setUsername("user");
        jwtRequestModel.setPassword("password");

        mvc.perform(post("/service/login").contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsBytes(jwtRequestModel)))
                .andExpect(status().isUnauthorized());


    }

    @Test
    public void createToken_userDisabled() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new DisabledException(""));

        JwtRequestModel jwtRequestModel = new JwtRequestModel();
        jwtRequestModel.setUsername("user");
        jwtRequestModel.setPassword("password");

        mvc.perform(post("/service/login").contentType(MediaType.APPLICATION_JSON).content(Json.getInstance().getObjectMapper().writeValueAsBytes(jwtRequestModel)))
                .andExpect(status().isUnauthorized());


    }
}