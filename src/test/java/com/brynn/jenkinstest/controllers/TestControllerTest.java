package com.brynn.jenkinstest.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("User 5")))
                .andExpect(jsonPath("$.email", is("user5@example.com")));
    }

    @Test
    public void testGetUserById_NegativeId() throws Exception {
        mockMvc.perform(get("/api/users/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(-1)))
                .andExpect(jsonPath("$.name", is("User -1")))
                .andExpect(jsonPath("$.email", is("user-1@example.com")));
    }

    @Test
    public void testGetUserById_ZeroId() throws Exception {
        mockMvc.perform(get("/api/users/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)));
    }

    @Test
    public void testGetUserById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/users/abc"))
                .andExpect(status().isBadRequest()); // Spring will return 400
    }

    @Test
    public void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/users/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }
}
