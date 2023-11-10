package com.daim.blog.infrastructure.jwt;

import com.daim.blog.BlogBackendApplication;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {BlogBackendApplication.class})
@AutoConfigureMockMvc
public class JwtFilterTest {

    @Autowired
    private JwtFilter jwtFilter;

    @MockBean
    private TokenManager tokenManager;

    @Test
    public void doFilterInternal() throws ServletException, IOException {
        Mockito.when(tokenManager.getUsernameFromToken("illegal")).thenThrow(new IllegalArgumentException());
        Mockito.when(tokenManager.getUsernameFromToken("expired")).thenThrow(new ExpiredJwtException(null, null, null));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer illegal");
        jwtFilter.doFilterInternal(request, response, filterChain);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        request.addHeader("Authorization", "Bearer expired");
        jwtFilter.doFilterInternal(request, response, filterChain);
    }
}