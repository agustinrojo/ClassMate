package com.example.Security.service;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    private static final String EMAIL_REGEX = "^[0-9]{1,6}@(sistemas|industrial|mecanica|electrica|electronica|quimica)\\.frc\\.utn\\.edu\\.ar$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean test(String email) {
        if(email == null){
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
