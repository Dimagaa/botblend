package com.app.botblend.validation;

public final class ValidationConstants {
    public static final String PASSWORD_REGEXP =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
    public static final String INVALID_PASSWORD_MESSAGE =
            "Minimum eight characters, at least one uppercase"
            + " letter, one lowercase letter and one number";
}
