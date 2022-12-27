package constants;

public interface RegexConstants {
    String PASSWORD_REGEX = "(?=.*\\d)(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\W).{6,20}";
}
