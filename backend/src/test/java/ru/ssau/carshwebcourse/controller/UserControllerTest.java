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
    private RoleRepository roleRepository;

    private Long savedUserId;
    @BeforeEach
    public void before(){
        Role role = new Role();
        role.setRoleName(UserRole.ROLE_ADMIN);
        roleRepository.save(role);
        Role role2 = new Role();
        role2.setRoleName(UserRole.ROLE_USER);
        roleRepository.save(role2);
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstname("TestName");
        testUser.setLastname("TestLastname");
        testUser.setPassword("1234");
        savedUserId = userRepository.save(testUser).getUserId();
    }

    @AfterEach
    public void after(){
        userRepository.deleteAllInBatch();
    }

    @Test
    void userRegisterTest(){
        UserDto user = new UserDto();
        user.setEmail("testUser@example.com");
        user.setPassword("1234");
        userController.userRegister(user);
        UserDto user1 = userController.getUserByUserId(savedUserId).getBody();
        assertEquals(savedUserId,user1.getUserId());
        assertEquals("test@example.com",user1.getEmail());
    }

    @Test
    void updateUserTest(){
        UserDto user = new UserDto();
        user.setEmail("updateTestUser@example.com");
        user.setFirstname("updateTestName");
        user.setLastname("updateTestLastname");
        user.setPassword("1234");
        UserDto user1 = userController.updateUser("test@example.com", user).getBody();
        assertEquals("updateTestUser@example.com",user1.getEmail());
        assertEquals("updateTestName",user1.getFirstname());
        assertEquals("updateTestLastname",user1.getLastname());
    }
}
