package com.daim.blog.service.implementation;

import com.daim.blog.BlogBackendApplication;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BlogBackendApplication.class})
@AutoConfigureMockMvc
public class UserDetailsServiceImpTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername() {
        //Preparation
        UserEntity userEntity = new UserEntity("username", "name", "email", "url");
        userEntity.setPassword("password");
        userRepository.save(userEntity);

        //Exec and assert
        UserDetails user = userDetailsService.loadUserByUsername("username");
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("null");
        });

        //clean up
        userRepository.deleteAll();
    }
}