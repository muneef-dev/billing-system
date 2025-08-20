package com.example.billingsystem.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GeneratorUtil {

    private static final Random random = new Random();

    /**
     * Generate account number: ACC + timestamp + random digits
     * Example: ACC202508211234567890
     */
    public static String generateAccountNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomDigits = String.format("%04d", random.nextInt(10000));
        return "ACC" + timestamp + randomDigits;
    }

    /**
     * Generate item code: ITM + random alphanumeric
     * Example: ITM7A9B2C4D
     */
    public static String generateItemCode() {
        return "ITM" + generateRandomAlphaNumeric(8);
    }

    /**
     * Generate order number: ORD + date + sequential number
     * Example: ORD20250821001
     */
    public static String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequential = String.format("%03d", random.nextInt(1000));
        return "ORD" + date + sequential;
    }

    /**
     * Generate reference number: REF + UUID (first 12 chars)
     * Example: REF7A9B2C4D5E6F
     */
    public static String generateReferenceNumber() {
        return "REF" + KeyGenerator.generateId().substring(0, 12).toUpperCase();
    }

    private static String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
}
