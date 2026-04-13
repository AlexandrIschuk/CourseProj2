package ru.ssau.carshwebcourse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.DriveStatus;
import ru.ssau.carshwebcourse.entity.User;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;
import ru.ssau.carshwebcourse.service.DriveService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/drives")
public class DriveController {
    private final DriveService driveService;
    private final CustomUserDetailsService customUserDetailsService;

    public DriveController(DriveService driveService, CustomUserDetailsService customUserDetailsService){
        this.driveService = driveService;
        this.customUserDetailsService = customUserDetailsService;
    }
    @PostMapping
    public ResponseEntity<DriveDto> startDrive(@RequestBody DriveDto driveDto, @AuthenticationPrincipal UserDetails userDetails){
        driveDto.setUserId(customUserDetailsService.getUserByEmail(userDetails.getUsername()).getUserId());
        return new ResponseEntity<>(driveService.startDrive(driveDto), HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public List<DriveDto> getHistoryDrivesByUser(@AuthenticationPrincipal UserDetails userDetails){
        UserDto user = customUserDetailsService.getUserByEmail(userDetails.getUsername());
        return driveService.getHistoryDrivesByUser(user);
    }
    @GetMapping("/all")
    @ResponseBody
    public List<DriveDto> findAllDrives(){
        return driveService.findAllDrives();
    }
    @GetMapping("/active")
    @ResponseBody
    public List<DriveDto> findActiveDrives(@AuthenticationPrincipal UserDetails userDetails ){
        UserDto user = customUserDetailsService.getUserByEmail(userDetails.getUsername());
        return driveService.findActiveDrives(user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DriveDto> completeDrive(@PathVariable Long id,@RequestBody DriveDto drive){

        return new ResponseEntity<>(driveService.updateDriveStatus(id,drive),HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<DriveDto> cancelDrive(@PathVariable Long id){
        return new ResponseEntity<>(driveService.cancelDrive(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDrive(@PathVariable Long id){
        driveService.deleteDrive(id);
    }
}
