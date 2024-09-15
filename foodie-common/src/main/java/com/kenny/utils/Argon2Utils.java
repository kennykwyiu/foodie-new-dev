package com.kenny.utils;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class Argon2Utils {

    static final int ITERATIONS = 2;
    static final int MEM_LIMIT = 66536;
    static final int HASH_LENGTH = 32;
    static final int PARALLELISM = 1;
    public static String hashPassword(String password) {
        byte[] salt = generateSalt16Byte();

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEM_LIMIT)
                .withParallelism(PARALLELISM)
                .withSalt(salt);

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[HASH_LENGTH];
        generator.generateBytes(passwordBytes, result, 0, result.length);
        return Hex.toHexString(salt) + Hex.toHexString(result);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        byte[] hashedPasswordBytes = Hex.decode(hashedPassword.substring(32));
        byte[] salt = Hex.decode(hashedPassword.substring(0, 32)); // Extract salt

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withMemoryAsKB(MEM_LIMIT) // Should match the memory cost used during hashing
                .withParallelism(PARALLELISM)
                .withIterations(ITERATIONS) // Should match the iterations used during hashing
                .withSalt(salt);  // Extract the original salt from the hashed password

        Argon2BytesGenerator verifier = new Argon2BytesGenerator();
        verifier.init(builder.build());

        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] testHash = new byte[HASH_LENGTH];
        verifier.generateBytes(passwordBytes, testHash, 0, HASH_LENGTH);

        // Compare the generated hash with the stored hashed password bytes
        return Arrays.equals(hashedPasswordBytes, testHash);
    }

    private static byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static void main(String[] args) {
        String password = "123456";
        String hashedPassword = hashPassword(password);
        System.out.println("Hashed Password: " + hashedPassword);

        boolean isValid = verifyPassword(password, hashedPassword);
        System.out.println("Is Password Valid? " + isValid);
    }
}
