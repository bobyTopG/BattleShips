package be.kdg.integration1.battleships_solitaire.logic;

import org.postgresql.util.PGInterval;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;

public final class Utility {

    public static char convertCoordinate(int n) {
        return (char) (n + 'A' - 1);
    }

    public static int convertCoordinate(char c) {
        return c - 'A' + 1;
    }

    public static PGInterval convert(Duration duration) {
        return new PGInterval(0, 0, 0, 0, 0, duration.getSeconds());
    }

    public static boolean isXNotInBounds(int x, int limit) {
        return x < 1 || x > limit;
    }

    public static boolean isYNotInBounds(char y, int limit) {
        return y < 'A' || y > 'A' + limit - 1;
    }

    public static void repeat(int n, Runnable action) {
        for (int i = 0; i < n; i++) {
            action.run();
        }
    }

    // Hashes a password
    public static String hashPassword(String password) {
        if (password == null) { return null; }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: ", e);
        }
    }

    public static boolean validatePassword(String enteredPassword, String hashedPassword) {
        if (enteredPassword == null) { return true; }
        return hashPassword(enteredPassword).equals(hashedPassword);
    }

}