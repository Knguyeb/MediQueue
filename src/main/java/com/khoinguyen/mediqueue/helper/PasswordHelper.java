package com.khoinguyen.mediqueue.helper;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordHelper {

    private static final int SALT_SIZE = 16;   // 128 bit (Tính theo byte)
    private static final int KEY_SIZE = 256;  // 256 bit (Trong Java cấu hình PBEKeySpec tính theo bit)
    private static final int ITERATIONS = 100000;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    // Hash mật khẩu với PBKDF2 + Salt
    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_SIZE];
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // Lưu dạng: iterations:salt:hash
            return ITERATIONS + ":" + 
                   Base64.getEncoder().encodeToString(salt) + ":" + 
                   Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu", e);
        }
    }

    // Kiểm tra mật khẩu nhập vào
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String[] parts = storedHash.split(":");
        if (parts.length != 3) return false;

        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] storedKey = Base64.getDecoder().decode(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), salt, iterations, KEY_SIZE);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] key = skf.generateSecret(spec).getEncoded();

            // MessageDigest.isEqual giúp so sánh mảng byte trong thời gian cố định (FixedTimeEquals)
            // Ngăn chặn tấn công Timing Attack
            return MessageDigest.isEqual(key, storedKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            return false;
        }
    }
}