package com.juniorstart.juniorstart.util;

public abstract class UserIdGenerator {
    private static Long id = 10000000L;

    public static Long generateId() {
        return id++;
    }
}
