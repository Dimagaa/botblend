package com.app.botblend.contoller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.botblend.dto.user.UserDto;
import com.app.botblend.dto.user.UserRegisterRequestDto;
import com.app.botblend.model.User;
import com.app.botblend.utils.TestUserUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;
    private static final User AUTHENTICATED_USER = TestUserUtil.USER_DATA.get(1L);
    private static final String AUTHENTICATED_USER_EMAIL = "bob@test.app";
    private static final List<UserRegisterRequestDto> USER_REGISTER_BAD_REQUEST = List.of(
            new UserRegisterRequestDto(
                    "",
                    "Pwd12345",
                    "Pwd12345",
                    "Bob",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "invalidemail",
                    "Pwd12345",
                    "Pwd12345",
                    "Bob",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    null,
                    "",
                    "Bob",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd12345",
                    "Pwd123456",
                    "Bob",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd1",
                    "Pwd1",
                    "Bob",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "VeryLongPassword",
                    "VeryLongPassword",
                    "Bob", "Smith"),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd12345",
                    "Pwd12345",
                    "",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd12345",
                    "Pwd12345",
                    "A",
                    "Smith"
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd12345",
                    "Pwd12345",
                    "Bob",
                    ""
            ),
            new UserRegisterRequestDto(
                    "user@example.com",
                    "Pwd12345",
                    "Pwd12345",
                    "Bob",
                    "S"
            )
    );

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Get Current User: Admin User - Return UserDto")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCurrent_AdminUser_ReturnsUserDto() {
        UserDto expected = new UserDto(
                AUTHENTICATED_USER.getId(),
                AUTHENTICATED_USER.getEmail(),
                AUTHENTICATED_USER.getFirstName(),
                AUTHENTICATED_USER.getLastName()
        );

        MockHttpServletResponse response = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        UserDto actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Get Current User: Non-Authenticated - Return 401 Forbidden")
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCurrent_NonAuthenticated_Returns401() {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @DisplayName("Register User: Valid Request - Return UserDto")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_ValidRequest_ReturnsUserDto() {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto(
                "frodo@test.app",
                TestUserUtil.PASSWORD,
                TestUserUtil.PASSWORD,
                "Frodo",
                "Baggins"
        );
        String request = objectMapper.writeValueAsString(requestDto);

        UserDto expected = new UserDto(null,
                requestDto.email(),
                requestDto.firstName(),
                requestDto.lastName());

        MockHttpServletResponse response = mockMvc.perform(
                        post("/users/register")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        UserDto actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @TestFactory
    @DisplayName("Register User: Invalid Request - Return 400 Bad Request")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    Stream<DynamicTest> register_InvalidRequest_ReturnsHttp400BadRequest() {
        return USER_REGISTER_BAD_REQUEST.stream()
                .map(requestDto -> DynamicTest.dynamicTest(
                        "Try register user: " + requestDto,
                        () -> {
                            String request = objectMapper.writeValueAsString(requestDto);
                            mockMvc.perform(post("/users/register")
                                            .content(request)
                                            .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isBadRequest());
                        }
                ));
    }

    @SneakyThrows
    @Test
    @DisplayName("Update User: Admin User - Return 200 Ok")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_AdminUser_Returns200() {
        Long id = AUTHENTICATED_USER.getId();
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto(
                AUTHENTICATED_USER_EMAIL,
                TestUserUtil.PASSWORD,
                TestUserUtil.PASSWORD,
                "UpdatedFirstName",
                "UpdatedLastName"
        );
        String request = objectMapper.writeValueAsString(requestDto);

        UserDto expected = new UserDto(id,
                requestDto.email(),
                requestDto.firstName(),
                requestDto.lastName()
        );

        MockHttpServletResponse response = mockMvc.perform(
                        put("/users")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        UserDto actual = objectMapper.readValue(
                response.getContentAsByteArray(),
                new TypeReference<>() {}
        );

        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Delete User: Admin User - Return 204 No Content")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_AdminUser_ReturnsHttp204NoContent() {
        long id = 2;
        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    @DisplayName("Delete User: Unauthorized User - Return 401")
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_UnauthorizedUser_Returns401() {
        long id = 1;
        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @DisplayName("Delete User: User ID Not Found - Return 404 Not Found")
    @WithUserDetails(AUTHENTICATED_USER_EMAIL)
    @Sql(scripts = {
            TestUserUtil.DELETE_USER_DATA,
            TestUserUtil.INSERT_USER_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_UserIdNotFound_ReturnsHttp404NotFound() {
        long id = -99;
        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNotFound());
    }
}
