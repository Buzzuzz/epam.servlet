package utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Util class created for password hashing with help of argon2 algorithm.
 * @see <a href="https://en.wikipedia.org/wiki/Argon2">Argon2</a> for more information
 */
public class PasswordHashUtil {
    private static final Argon2 argon2 = Argon2Factory.create();
    private static final int ITERATIONS = 2;
    private static final int MEMORY = 15 * 1024;
    private static final int PARALLELISM = 1;

    /**
     * Method to encrypt the password with argon2 algorithm
     * @param password Value to encrypt
     * @return Hash {@link String} of given value
     */
    public static String encode(String password) {
        return password != null ? argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray()) : "";
    }

    /**
     * Method to check given value with hash string
     * @param hash Hash of some value to check against
     * @param password Value to check against hash
     * @return true if hash belongs to given value, false otherwise
     */
    public static boolean verify(String hash, String password) {
        return argon2.verify(hash, password.toCharArray());
    }

    // Suppress constructor
    private PasswordHashUtil() {
    }
}
