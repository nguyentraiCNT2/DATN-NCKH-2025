package org.ninhngoctuan.backend.config;


import java.security.SecureRandom;

public class RandomId {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMBERTERS = "0123456789";
    public static String generateRandomId(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder randomId = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomId.append(randomChar);
        }

        return randomId.toString();
    }
    public static String generateMKC2(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder randomMKC2 = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(NUMBERTERS.length());
            char randomChar = NUMBERTERS.charAt(randomIndex);
            randomMKC2.append(randomChar);
        }

        return randomMKC2.toString();
    }
    public static void main(String[] args) {
        // Generate a random ID with a minimum length of 50 characters
        String randomId = generateRandomId(20);
        System.out.println("Random ID: " + randomId);
        String MKC2 = generateMKC2(11);
        System.out.println("Random MKC2: " + MKC2);
    }
}