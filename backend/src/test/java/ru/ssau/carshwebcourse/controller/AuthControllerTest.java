package ru.ssau.carshwebcourse.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.DriveStatus;
import ru.ssau.carshwebcourse.entity.User;
import ru.ssau.carshwebcourse.repository.UserRepository;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;

    private UserDetails userDet;
    private User testUser;

    @BeforeEach
    public void before() {
        testUser = new User();
        testUser.setEmail("authMeTest@example.com");
        testUser.setPassword("password");
        testUser.setFirstname("TestFirstname");
        testUser.setLastname("TestLastname");
        testUser.setPhoneNumber("9023131232");
        testUser = userRepository.save(testUser);
        Set<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        userDet = new org.springframework.security.core.userdetails.User(testUser.getEmail(), testUser.getPassword(),roles);

    }

    @Test
    void authMeTest() {
        UserDto result = authController.getAuthUser(userDet).getBody();

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstname(), result.getFirstname());
        assertEquals(testUser.getLastname(), result.getLastname());
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
    }
}
