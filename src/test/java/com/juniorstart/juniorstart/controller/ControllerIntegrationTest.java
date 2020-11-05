package com.juniorstart.juniorstart.controller;

import com.google.gson.Gson;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.repository.UserDao;
import io.swagger.models.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public abstract class ControllerIntegrationTest {

    protected final String TEST_NAME = "Test Name";

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    protected Gson gson = new Gson();


    @Autowired
    UserDao userDao;


    protected User admin;
    protected User user;

    @BeforeEach
    void setUp() throws Exception {
        user = userDao.findByEmail("someEmail").orElseThrow();

    }

    protected MvcResult useBasicMvc(HttpMethod httpMethod, String url,
                                    Integer status) throws Exception {
        switch (httpMethod) {
            case POST:
                return mockMvc.perform(post(url))
                        .andExpect(status().is(status)).andReturn();
            case GET:
                return mockMvc.perform(get(url))
                        .andExpect(status().is(status)).andReturn();
            case PUT:
                return mockMvc.perform(put(url))
                        .andExpect(status().is(status)).andReturn();
            case DELETE:
                return mockMvc.perform(delete(url))
                        .andExpect(status().is(status)).andReturn();
            default:
                throw new RuntimeException();
        }
    }
}
