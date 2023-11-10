package com.example.azurefunctiontc;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.function.Function;

@Component
public class Uppercase implements Function<String, String> {
    @Override
    public String apply(String s) {
        return s.toUpperCase(Locale.getDefault());
    }
}
