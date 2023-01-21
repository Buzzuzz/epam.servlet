package com.servlet.ejournal.constants;

public interface RegexConstants {
    String PASSWORD_REGEX = "[\\w[а-яА-Я][!@#$%^&*()=+~`'/\\\\<>?\\[\\\\]]{6,20}";
    String PHONE_NUMBER_REGEX = "[\\d]{9}";
    String IS_A_NUMBER = "[\\d]+";
}
