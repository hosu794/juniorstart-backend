package com.juniorstart.juniorstart.generation;

public abstract class UserIdGenerator {
    private static Long id = 10000000L;

    public static Long generateId() {
        return id++;
    }
}
