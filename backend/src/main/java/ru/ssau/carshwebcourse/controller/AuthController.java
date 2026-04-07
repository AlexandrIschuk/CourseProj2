package ru.ssau.carshwebcourse.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.AuthResponse;
import ru.ssau.carshwebcourse.entity.Role;
import ru.ssau.carshwebcourse.entity.UserRole;
import ru.ssau.carshwebcourse.repository.UserRepository;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;
import ru.ssau.carshwebcourse.service.TokenService;

import javax.naming.AuthenticationException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> jwtLogin(@RequestBody UserDto user) throws NoSuchAlgorithmException, InvalidKeyException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        AuthResponse authToken = new AuthResponse(tokenService.generateToken(userDetails),tokenService.generateRefreshToken(userDetails));
        return ResponseEntity.ok().body(authToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getAuthUser(@AuthenticationPrincipal UserDetails user) {
        UserDto userDto = new UserDto();
        Set<Role> roles = user.getAuthorities().stream()
                .map(role -> new Role(UserRole.valueOf(role.getAuthority())))
                .collect(Collectors.toSet());
        userDto = userDetailsService.getUserByEmail(user.getUsername());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody String token) throws NoSuchAlgorithmException, InvalidKeyException, AuthenticationException {
        Map<String,Object> payload = tokenService.getDecodePayload(token);
        String email = payload.get("email").toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        AuthResponse authToken = new AuthResponse(tokenService.generateToken(userDetails),null);
        return ResponseEntity.ok(authToken);
    }
}
