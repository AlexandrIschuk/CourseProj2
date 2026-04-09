package ru.ssau.carshwebcourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.exceptionHandler.CarIsNotFreeException;
import ru.ssau.carshwebcourse.mapping.DriveMappingUtils;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.DriveRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DriveServiceTest {
    private DriveRepository driveRepository;
    private CarRepository carRepository;
    private DriveMappingUtils driveMappingUtils;
    private UserMappingUtils userMappingUtils;
    private DriveService driveService;

    private Car car1;
    private Car car2;
    private User user;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
        driveRepository = mock(DriveRepository.class);
        carRepository = mock(CarRepository.class);
        driveMappingUtils = mock(DriveMappingUtils.class);
        userMappingUtils = mock(UserMappingUtils.class);

        car1 = new Car();
        car1.setCarId(1L);
        car1.setCarStatus(CarStatus.FREE);

        car2 = new Car();
        car2.setCarId(1L);
        car2.setCarStatus(CarStatus.ON_A_TRIP);

        user = new User();
        user.setUserId(1L);

        userDto = new UserDto();
        userDto.setUserId(1L);

        driveService = new DriveService(carRepository, driveMappingUtils, mock(UserMappingUtils.class),
                driveRepository, mock(TariffRepository.class));
    }

    @Test
    void testStartDrive() {
        DriveDto inputDto = new DriveDto();
        inputDto.setUserId(1L);

        Drive entity = new Drive();
        entity.setUser(user);
        entity.setCar(car1);

        when(driveRepository.countDrivesByDriveStatusAndUser_UserId(DriveStatus.ACTIVE, 1L)).thenReturn(0L);
        when(driveMappingUtils.mapToDriveEntity(any())).thenReturn(entity);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
        when(driveRepository.save(any())).thenReturn(new Drive());
        when(driveMappingUtils.mapToDriveDto(any())).thenReturn(new DriveDto());

        DriveDto result = driveService.startDrive(inputDto);

        assertNotNull(result);

    }

    @Test
    void testStartDrive_CarIsNotFree() {
        DriveDto inputDto = new DriveDto();
        inputDto.setUserId(1L);

        Drive entity = new Drive();
        entity.setUser(user);
        entity.setCar(car2);

        when(driveRepository.countDrivesByDriveStatusAndUser_UserId(DriveStatus.ACTIVE, 1L)).thenReturn(0L);
        when(driveMappingUtils.mapToDriveEntity(any())).thenReturn(entity);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car2));
        when(driveRepository.save(any())).thenReturn(new Drive());
        when(driveMappingUtils.mapToDriveDto(any())).thenReturn(new DriveDto());

        assertThrows(CarIsNotFreeException.class, () -> driveService.startDrive(inputDto));

    }

    @Test
    void testFindAllDrives() {
        when(driveRepository.findAll()).thenReturn(List.of(new Drive(), new Drive()));
        when(driveMappingUtils.mapToDriveDto(any())).thenReturn(new DriveDto());

        List<DriveDto> result = driveService.findAllDrives();

        assertEquals(2, result.size());
    }

    @Test
    void testCompleteDrive() {
        Drive drive = new Drive();
        drive.setDriveStatus(DriveStatus.ACTIVE);

        drive.setCar(car2);

        when(driveRepository.findById(1L)).thenReturn(Optional.of(drive));

        driveService.completeDrive(1L);

        assertEquals(DriveStatus.COMPLETED, drive.getDriveStatus());
        assertEquals(CarStatus.FREE, car2.getCarStatus());

    }

    @Test
    void testCancelDrive() {
        Drive drive = new Drive();
        drive.setStartRental(LocalDateTime.now().minusMinutes(3));
        drive.setDriveStatus(DriveStatus.ACTIVE);

        Drive drive1 = new Drive();
        drive1.setStartRental(LocalDateTime.now().minusMinutes(7));
        drive1.setDriveStatus(DriveStatus.ACTIVE);

        drive.setCar(car2);

        when(driveRepository.findById(1L)).thenReturn(Optional.of(drive));
        when(driveRepository.findById(2L)).thenReturn(Optional.of(drive1));
        when(driveMappingUtils.mapToDriveDto(any())).thenReturn(new DriveDto());

        DriveDto result = driveService.cancelDrive(1L);

        assertNotNull(result);
        assertEquals(DriveStatus.CANCELLED, drive.getDriveStatus());
        assertEquals(CarStatus.FREE, car2.getCarStatus());

        assertThrows(IllegalStateException.class, () -> driveService.cancelDrive(2L));
    }

//    @Test
//    void testFindAllDrivesByUser() {
//        Drive drive1 = new Drive();
//        drive1.setDriveStatus(DriveStatus.COMPLETED);
//        drive1.setUser(user);
//
//        Drive drive2 = new Drive();
//        drive2.setDriveStatus(DriveStatus.CANCELLED);
//        drive2.setUser(user);
//
//        List<Drive> drives = List.of(drive1, drive2);
//
//        when(userMappingUtils.mapToUserEntity(userDto)).thenReturn(user);
//        when(driveRepository.findDriveHistoryByUser(any(User.class))).thenReturn(drives);
//        when(driveMappingUtils.mapToDriveDto(any())).thenReturn(new DriveDto());
//
//        List<DriveDto> result = driveService.findAllDrivesByUser(userDto);
//
//        assertEquals(2, result.size());
//    }

}
