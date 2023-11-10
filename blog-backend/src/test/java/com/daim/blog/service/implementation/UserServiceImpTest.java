package com.daim.blog.service.implementation;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.model.UserRegisterModel;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BlogBackendApplication.class})
@AutoConfigureMockMvc
public class UserServiceImpTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void register() {
        //Preparation
        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setPassword("password");
        userRegisterModel.setUsername("username");
        userRegisterModel.setName("name");
        userRegisterModel.setEmail("email");
        userRegisterModel.setUrlPath("url");

        //Execution
        userService.register(userRegisterModel);

        //Assertion
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();

        assertSame(1, userEntities.size());
        assertEquals("username", userEntities.get(0).getUsername());
        assertEquals("name", userEntities.get(0).getName());
        assertEquals("email", userEntities.get(0).getEmail());
        assertEquals("url", userEntities.get(0).getUrlPath());

        userRepository.deleteAll();
    }
}