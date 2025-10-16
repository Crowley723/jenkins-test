package com.brynn.jenkinstest.controllers;

import com.brynn.jenkinstest.model.Customer;
import com.brynn.jenkinstest.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class TestControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        TestController testController = new TestController(customerRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    public void testCreateNewCustomer_Success() throws Exception {
        Customer customer = new Customer("John", "Doe");
        customer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void testCreateNewCustomer_MissingLastName() throws Exception {
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Doe\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNewCustomer_MissingFirstName() throws Exception {
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }

    @Test
    public void testGetCustomer_Found() throws Exception {
        Customer customer = new Customer("John", "Doe");
        customer.setId(1L);

        when(customerRepository.findByFirstName("John")).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customer/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")));
    }

    @Test
    public void testGetCustomer_NotFound() throws Exception {
        when(customerRepository.findByFirstName("NonExistent")).thenReturn(List.of());

        mockMvc.perform(get("/api/customer/NonExistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }
}