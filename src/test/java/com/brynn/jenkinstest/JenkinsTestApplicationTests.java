package com.brynn.jenkinstest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
@ActiveProfiles("test")
class JenkinsTestApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {}

	@Test
	void databaseIsReachable() {
		try (Connection conn = dataSource.getConnection()) {
			assertTrue(conn.isValid(5), "Database connection should be valid");
		} catch (Exception e) {
			fail("Database connection failed: " + e.getMessage());
		}
	}

}
