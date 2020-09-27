package com.juniorstart.juniorstart.util;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;

import java.sql.Date;
import java.util.UUID;

public class TestMethods {

    private TestMethods() {
    }

    public static User getUser() {
        return User.builder()
                .privateId(UUID.randomUUID())
                .publicId(10L)
                .name("Test")
                .age(18)
                .hiddenFromSearch(false)
                .email("test@test.com")
                .imageUrl("test Url")
                .emailVerified(true)
                .password("Password")
                .provider(AuthProvider.local)
                .userStatus(UserStatus.OPEN)
                .providerId("id").build();
    }

    public static Goal getGoal() {
        return Goal.builder()
                .privateId(UUID.randomUUID())
                .title("Test Title")
                .description("Test Description")
                .endDate(Date.valueOf("2020-09-26"))
                .done(false).build();
    }
}
