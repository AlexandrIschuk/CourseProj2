package ru.ssau.carshwebcourse.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.DriveRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;
import ru.ssau.carshwebcourse.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class DriveControllerTest {



    @Autowired
    private DriveController driveController;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TariffRepository tariffRepository;

    private Car testCar;
    private Tariff testTariff;
    private UserDetails userDet;
    @BeforeEach
    public void before() {
        User testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        Set<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        userDet = new org.springframework.security.core.userdetails.User(testUser.getEmail(), testUser.getPassword(),roles);


        testCar = new Car();
        testCar.setCarBrand("BMW");
        testCar.setCarModel("X5");
        testCar.setCarStatus(CarStatus.FREE);
        testCar = carRepository.save(testCar);

        testTariff = new Tariff();
        testTariff.setName("Standard");
        testTariff.setPrice(100L);
        testTariff.setTariffType(TariffType.MINUTE);
        testTariff = tariffRepository.save(testTariff);
    }

    @Test
    void testStartDrive() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        driveDto.setDriveStatus(DriveStatus.ACTIVE);

        DriveDto result = driveController.startDrive(driveDto, userDet).getBody();

        assertNotNull(result);
        assertNotNull(result.getDriveId());
        assertEquals(DriveStatus.ACTIVE, result.getDriveStatus());
    }

    @Test
    void testFindAllDrivesByUser() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        driveDto.setDriveStatus(DriveStatus.COMPLETED);
        driveController.startDrive(driveDto, userDet);

        List<DriveDto> drives = driveController.getHistoryDrivesByUser(userDet);

        assertNotNull(drives);
        assertEquals(1, drives.size());
    }

    @Test
    void testFindAllDrives() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        driveController.startDrive(driveDto, userDet);

        List<DriveDto> drives = driveController.findAllDrives();

        assertNotNull(drives);
        assertEquals(1, drives.size());
    }

    @Test
    void testFindActiveDrives() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        driveDto.setDriveStatus(DriveStatus.ACTIVE);
        driveController.startDrive(driveDto, userDet);

        List<DriveDto> activeDrives = driveController.findActiveDrives(userDet);

        assertNotNull(activeDrives);
        assertEquals(1, activeDrives.size());
        assertEquals(DriveStatus.ACTIVE, activeDrives.get(0).getDriveStatus());
    }

    @Test
    void testCompleteDrive() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        DriveDto createdDrive = driveController.startDrive(driveDto, userDet).getBody();
        Long driveId = createdDrive.getDriveId();

        DriveDto completeDto = new DriveDto();
        completeDto.setDriveStatus(DriveStatus.PENDING_PAYMENT);
        completeDto.setTotalDistance(50L);

        DriveDto result = driveController.completeDrive(driveId, completeDto).getBody();

        assertNotNull(result);
        assertEquals(DriveStatus.PENDING_PAYMENT, result.getDriveStatus());
    }

    @Test
    void testCancelDrive() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        DriveDto createdDrive = driveController.startDrive(driveDto, userDet).getBody();
        Long driveId = createdDrive.getDriveId();

        DriveDto result = driveController.cancelDrive(driveId).getBody();

        assertNotNull(result);
        assertEquals(DriveStatus.CANCELLED, result.getDriveStatus());
    }

    @Test
    void testDeleteDrive() {
        DriveDto driveDto = new DriveDto();
        driveDto.setCar(testCar);
        driveDto.setTariff(testTariff);
        DriveDto createdDrive = driveController.startDrive(driveDto, userDet).getBody();
        Long driveId = createdDrive.getDriveId();

        driveController.deleteDrive(driveId);

        assertTrue(driveRepository.findById(driveId).isEmpty());
    }
}
