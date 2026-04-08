package ru.ssau.carshwebcourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.Role;
import ru.ssau.carshwebcourse.entity.User;
import ru.ssau.carshwebcourse.entity.UserRole;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private UserMappingUtils userMappingUtils;
    private BCryptPasswordEncoder passwordEncoder;
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMappingUtils = mock(UserMappingUtils.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository, userMappingUtils, passwordEncoder);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("1234");
        testUser.setRole(Collections.singleton(new Role(2L, UserRole.ROLE_USER)));

        testUserDto = new UserDto();
        testUserDto.setEmail("test@example.com");
        testUserDto.setPassword("1234");
        testUser.setRole(Collections.singleton(new Role(2L, UserRole.ROLE_USER)));

    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());

        Set<? extends GrantedAuthority> authorities = (Set<? extends GrantedAuthority>) userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("notfound@example.com"));
    }

    @Test
    void testUserRegister() {
        UserDto inputDto = new UserDto();
        inputDto.setEmail("new@example.com");
        inputDto.setPassword("rawPassword");
        inputDto.setPhoneNumber("9876543210");

        User userEntity = new User();
        userEntity.setEmail("new@example.com");
        userEntity.setPassword("rawPassword");
        userEntity.setPhoneNumber("9876543210");


        when(userMappingUtils.mapToUserEntity(inputDto)).thenReturn(userEntity);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        customUserDetailsService.userRegister(inputDto);

        assertEquals("+79876543210", inputDto.getPhoneNumber());

        assertEquals("encodedPassword", userEntity.getPassword());

        assertNotNull(userEntity.getRole());
        assertEquals(1, userEntity.getRole().size());
        assertEquals(UserRole.ROLE_USER, userEntity.getRole().iterator().next().getRoleName());
    }

}