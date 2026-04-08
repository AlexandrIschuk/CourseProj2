package ru.ssau.carshwebcourse.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.Role;
import ru.ssau.carshwebcourse.entity.User;
import ru.ssau.carshwebcourse.entity.UserRole;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.UserRepository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMappingUtils userMappingUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, UserMappingUtils userMappingUtils, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMappingUtils = userMappingUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не существует!"));
        Set<GrantedAuthority> roles = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),roles);
    }


    public void userRegister(UserDto userDto){
        userDto.setPhoneNumber("+7" + userDto.getPhoneNumber());
        User user = userMappingUtils.mapToUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Collections.singleton(new Role(2L, UserRole.ROLE_USER)));
        userRepository.save(user);
    }

    public UserDto getUserByEmail(String email){
        return userMappingUtils.mapToUserDto(userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь не существует!")));
    }
    public UserDto getUserByUserId(Long id){
        return userMappingUtils.mapToUserDto(userRepository.getUsersByUserId(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь не существует!")));
    }
}
