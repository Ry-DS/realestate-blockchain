package com.rmit.realestate.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Hashing {
    public static String hash(Object... objs) {
        String input = Arrays.stream(objs).map(obj -> obj == null ? "null" : obj.toString()).collect(Collectors.joining());
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to hash: " + input);
        }
    }
}
