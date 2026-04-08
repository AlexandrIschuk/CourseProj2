package ru.ssau.carshwebcourse.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.repository.RoleRepository;
import ru.ssau.carshwebcourse.repository.UserRepository;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void before(){
        Role role = new Role();
        role.setRoleName(UserRole.ROLE_ADMIN);
        roleRepository.save(role);
        Role role2 = new Role();
        role2.setRoleName(UserRole.ROLE_USER);
        roleRepository.save(role2);
    }

    @AfterEach
    public void after(){
        userRepository.deleteAllInBatch();
    }

    @Test
    void userRegisterTest(){
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setPassword("1234");
        userController.userRegister(user);
        UserDto user1 = userController.getUserByUserId(1L).getBody();
        assertEquals(1L,user1.getUserId());
        assertEquals("test@example.com",user1.getEmail());
    }
}
