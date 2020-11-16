package com.juniorstart.juniorstart.bootstrap;

import com.juniorstart.juniorstart.model.AuthProvider;
import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import com.juniorstart.juniorstart.repository.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationLoader implements CommandLineRunner {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        runBootstrap();
    }

    public void runBootstrap() {
        log.info("Starting bootstrap loader");
        boolean checkDb = checkBootstrap();
        loadTaskTimerLoader(checkDb);
        log.info("Bootstrap has been loaded");
    }

    private boolean checkBootstrap() {
        boolean isUser = userDao.findAll().isEmpty();

        return isUser;
    }

    private void loadTaskTimerLoader(boolean isTrue) {
        if (isTrue) {
            User user = User.builder().email("someEmail")
                    .userStatus(UserStatus.LOOKING_FOR_A_JOB)
                    .imageUrl("some_url")
                    .hiddenFromSearch(false)
                    .age(12)
                    .password(passwordEncoder.encode("Some password"))
                    .provider(AuthProvider.local)
                    .name("Somename")
                    .emailVerified(false)
                    .publicId(12l)
                    .privateId(new UUID(12l, 12l))
                    .providerId("dsadadsd asd sad")
                    .build();

            userDao.save(user);
        }
    }
}


