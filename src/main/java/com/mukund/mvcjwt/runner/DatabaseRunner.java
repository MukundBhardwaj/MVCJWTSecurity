package com.mukund.mvcjwt.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mukund.mvcjwt.dao.AuthUserRepository;
import com.mukund.mvcjwt.entity.AuthUser;

@Order(value = 1)
@Component
/**
 * {@code CommandLineRunner} class for initializing Database
 * 
 * @since 1.0
 * @author Mukund Bhardwaj
 */
public class DatabaseRunner implements CommandLineRunner {

    private AuthUserRepository authUserRepository;
    private PasswordEncoder passwordEncoder;

    public DatabaseRunner(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (authUserRepository.count() == 0) {
            authUserRepository.save(new AuthUser(null, "Mukund", "mukund@mail.com",
                    passwordEncoder.encode("Abc@1234"), "ROLE_ADMIN", true));
        }
    }

}
