package com.app.botblend.utils;

import com.app.botblend.model.Role;
import com.app.botblend.model.User;
import java.util.Map;
import java.util.Set;

public class TestUserUtil {
    public static final String INSERT_USER_DATA = "classpath:sqlscripts/insert-users.sql";
    public static final String DELETE_USER_DATA = "classpath:sqlscripts/delete-users.sql";
    public static final Role ROLE_ADMIN = new Role(1L, Role.RoleName.ROLE_ADMIN);
    public static final String PASSWORD = "Test1234";
    public static final String HASHED_PASSWORD =
            "$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK";
    public static final Map<Long, User> USER_DATA = Map.of(
            1L, new User(1L,
                    "bob@test.app",
                    HASHED_PASSWORD,
                    "Bob",
                    "Smith",
                    Set.of(ROLE_ADMIN),
                    false),

            2L, new User(2L,
                    "alice@test.app",
                    HASHED_PASSWORD,
                    "Alice",
                    "Smith",
                    Set.of(ROLE_ADMIN),
                    false),

            3L, new User(3L,
                    "john@test.app",
                    HASHED_PASSWORD,
                    "John",
                    "Lea",
                    Set.of(ROLE_ADMIN),
                    false)
    );
}
