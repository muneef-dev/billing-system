package com.example.billingsystem.util;

import java.util.UUID;

public class KeyGenerator {
    public static String generateId() {
//        System.out.println("Generated ID: " + UUID.randomUUID().toString());
        return UUID.randomUUID().toString();
    }
}
