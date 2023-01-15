package constants;

public interface RegexConstants {
    String PASSWORD_REGEX = "[\\w[а-яА-Я][!@#$%^&*()=+~`'/\\\\<>?\\[\\\\]]{6,20}";
    String PHONE_NUMBER_REGEX = "[\\d]{9}";
}
