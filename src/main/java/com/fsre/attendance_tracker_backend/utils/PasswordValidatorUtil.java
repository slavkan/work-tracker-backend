package com.fsre.attendance_tracker_backend.utils;

import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordValidatorUtil {

    public static void validatePassword(String password) throws IllegalArgumentException {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 100), // Password must be exactly 8 characters long
                new CharacterRule(EnglishCharacterData.UpperCase, 1), // At least one uppercase letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1), // At least one lowercase letter
                new CharacterRule(EnglishCharacterData.Digit, 1) // At least one digit
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new IllegalArgumentException("Password must meet following criteria: " +
                    String.join(", ", validator.getMessages(result)));
        }
    }
}