package com.app.botblend.utils;

import com.app.botblend.model.Role;
import com.app.botblend.model.User;
import java.util.Map;
import java.util.Set;

public class TestUserUtil {
    public static final String INSERT_USER_DATA = "classpath:sqlscripts/insert-users&roles.sql";
    public static final String DELETE_USER_DATA = "classpath:sqlscripts/delete-users&roles.sql";
    public static final Role ADMIN = new Role(1L, Role.RoleName.ROLE_ADMIN);
    public static final String PASSWORD = "Test1234";
    public static final String HASHED_PASSWORD =
            "$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK";
    public static final Map<Long, User> USER_DATA = Map.of(
            1L, new User(1L,
                    "bob@test.app",
                    HASHED_PASSWORD,
                    "Bob",
                    "Smith",
                    Set.of(ADMIN),
                    false),

            2L, new User(2L,
                    "alice@test.app",
                    HASHED_PASSWORD,
                    "Alice",
                    "Smith",
                    Set.of(ADMIN),
                    false),

            3L, new User(3L,
                    "john@test.app",
                    HASHED_PASSWORD,
                    "John",
                    "Lea",
                    Set.of(ADMIN),
                    false)
            );

    public static final User NON_ADMIN_USER = new User(4L,
            "harry@test.app",
            HASHED_PASSWORD,
            "Harry",
            "Potter",
            Set.of(),
            false);
}
