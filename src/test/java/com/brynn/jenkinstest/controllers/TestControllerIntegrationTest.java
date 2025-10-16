package com.brynn.jenkinstest.controllers;

import com.brynn.jenkinstest.model.Customer;
import com.brynn.jenkinstest.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateNewCustomer_Success() throws Exception {
        String customerJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\"}";


        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.id").exists());

        // Verify customer was actually saved to PostgreSQL
        assert customerRepository.findByFirstName("John").size() == 1;
    }

    @Test
    public void testCreateNewCustomer_MissingLastName() throws Exception {
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\"}"))
                .andExpect(status().isBadRequest());

        // Verify no customer was saved
        assert customerRepository.findByFirstName("John").isEmpty();
    }

    @Test
    public void testCreateNewCustomer_MissingFirstName() throws Exception {
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Doe\"}"))
                .andExpect(status().isBadRequest());

        // Verify database is still empty
        assert customerRepository.count() == 0;
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
        // Create and save a real customer to PostgreSQL
        Customer customer = new Customer("John", "Doe");
        customerRepository.save(customer);

        mockMvc.perform(get("/api/customer/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    public void testGetCustomer_NotFound() throws Exception {
        mockMvc.perform(get("/api/customer/NonExistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @Test
    public void testCreateAndRetrieveCustomer_EndToEnd() throws Exception {
        // Create a customer
        String customerJson = "{\"firstName\":\"Jane\",\"lastName\":\"Smith\"}";

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());

        // Retrieve the customer from PostgreSQL
        mockMvc.perform(get("/api/customer/Jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Jane")))
                .andExpect(jsonPath("$[0].lastName", is("Smith")));
    }

    @Test
    public void testMultipleCustomersWithSameFirstName() throws Exception {
        // Create multiple customers with same first name
        customerRepository.save(new Customer("John", "Doe"));
        customerRepository.save(new Customer("John", "Smith"));

        mockMvc.perform(get("/api/customer/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }
}