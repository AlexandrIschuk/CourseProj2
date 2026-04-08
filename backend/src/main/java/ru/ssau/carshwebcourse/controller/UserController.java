package ru.ssau.carshwebcourse.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;

@Controller
@RequestMapping("/users")
@Validated
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void userRegister(@Valid @RequestBody UserDto userDto){
        customUserDetailsService.userRegister(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable Long id){
        return new ResponseEntity<>(customUserDetailsService.getUserByUserId(id), HttpStatus.OK);
    }

}
