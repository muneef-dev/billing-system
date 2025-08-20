package com.example.billingsystem.util;

import java.util.UUID;

public class KeyGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
