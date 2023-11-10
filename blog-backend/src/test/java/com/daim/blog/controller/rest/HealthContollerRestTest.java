package com.daim.blog.controller.rest;

import com.daim.blog.BlogBackendApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BlogBackendApplication.class)
@AutoConfigureMockMvc
public class HealthContollerRestTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void check() throws Exception {
        mvc.perform(get("/service/ping"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Service is running !!!\"}"));
    }
}