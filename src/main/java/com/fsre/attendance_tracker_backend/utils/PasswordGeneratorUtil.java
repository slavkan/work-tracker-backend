package com.fsre.attendance_tracker_backend.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;
import java.util.List;

public class PasswordGeneratorUtil {

    public static String generateNewPassword() {
        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);

        List<CharacterRule> rules = Arrays.asList(lowerCaseRule, upperCaseRule, digitRule);
        return generator.generatePassword(8, rules);
    }
}