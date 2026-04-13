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

    @Test
    void testUpdateDriveStatus_ToPendingPayment_WithMinuteTariff() {
        Long driveId = 1L;

        Tariff tariff = new Tariff();
        tariff.setTariffType(TariffType.MINUTE);
        tariff.setPrice(100L);

        Drive existingDrive = new Drive();
        existingDrive.setDriveId(driveId);
        existingDrive.setDriveStatus(DriveStatus.ACTIVE);
        existingDrive.setTariff(tariff);
        existingDrive.setStartRental(LocalDateTime.now().minusMinutes(30));

        DriveDto updateDto = new DriveDto();
        updateDto.setDriveStatus(DriveStatus.PENDING_PAYMENT);

        when(driveRepository.findById(driveId)).thenReturn(Optional.of(existingDrive));
        when(driveRepository.save(existingDrive)).thenReturn(existingDrive);
        when(driveMappingUtils.mapToDriveDto(existingDrive)).thenReturn(new DriveDto());

        DriveDto result = driveService.updateDriveStatus(driveId, updateDto);

        assertNotNull(result);
        assertEquals(DriveStatus.PENDING_PAYMENT, existingDrive.getDriveStatus());
        assertNotNull(existingDrive.getEndRental());
        assertEquals(30L, existingDrive.getTotalDistance().longValue());
        assertEquals(3000L, existingDrive.getTotalAmount().longValue());

    }

    @Test
    void testUpdateDriveStatus_ToPendingPayment_WithKilometerTariff() {
        Long driveId = 1L;

        Tariff tariff = new Tariff();
        tariff.setTariffType(TariffType.KILOMETER);
        tariff.setPrice(50L);

        Drive existingDrive = new Drive();
        existingDrive.setDriveId(driveId);
        existingDrive.setDriveStatus(DriveStatus.ACTIVE);
        existingDrive.setTariff(tariff);
        existingDrive.setStartRental(LocalDateTime.now());

        DriveDto updateDto = new DriveDto();
        updateDto.setDriveStatus(DriveStatus.PENDING_PAYMENT);
        updateDto.setTotalDistance(100L);

        when(driveRepository.findById(driveId)).thenReturn(Optional.of(existingDrive));
        when(driveRepository.save(existingDrive)).thenReturn(existingDrive);
        when(driveMappingUtils.mapToDriveDto(existingDrive)).thenReturn(new DriveDto());

        DriveDto result = driveService.updateDriveStatus(driveId, updateDto);

        assertNotNull(result);
        assertEquals(DriveStatus.PENDING_PAYMENT, existingDrive.getDriveStatus());
        assertEquals(100L, existingDrive.getTotalDistance().longValue());
        assertEquals(5000L, existingDrive.getTotalAmount().longValue());

    }

    @Test
    void testUpdateDriveStatus_ToOtherStatus_NoCalculation() {
        Long driveId = 1L;

        Drive existingDrive = new Drive();
        existingDrive.setDriveId(driveId);
        existingDrive.setDriveStatus(DriveStatus.ACTIVE);

        DriveDto updateDto = new DriveDto();
        updateDto.setDriveStatus(DriveStatus.CANCELLED);

        when(driveRepository.findById(driveId)).thenReturn(Optional.of(existingDrive));
        when(driveRepository.save(existingDrive)).thenReturn(existingDrive);
        when(driveMappingUtils.mapToDriveDto(existingDrive)).thenReturn(new DriveDto());

        DriveDto result = driveService.updateDriveStatus(driveId, updateDto);

        assertNotNull(result);
        assertEquals(DriveStatus.CANCELLED, existingDrive.getDriveStatus());
        assertNull(existingDrive.getEndRental());
        assertNull(existingDrive.getTotalDistance());
        assertNull(existingDrive.getTotalAmount());

    }


//    @Test
//    void testFindAllDrivesByUser() {
//        // Arrange
//        User user = new User();
//        user.setUserId(1L);
//
//        Drive drive1 = new Drive();
//        drive1.setDriveId(1L);
//        drive1.setDriveStatus(DriveStatus.COMPLETED);
//
//        Drive drive2 = new Drive();
//        drive2.setDriveId(2L);
//        drive2.setDriveStatus(DriveStatus.CANCELLED);
//
//        List<Drive> drives = List.of(drive1, drive2);
//
//        when(userMappingUtils.mapToUserEntity(userDto)).thenReturn(user);
//        when(driveRepository.findDriveHistoryByUser(user)).thenReturn(drives);
//        when(driveMappingUtils.mapToDriveDto(any(Drive.class))).thenReturn(new DriveDto());
//
//        List<DriveDto> result = driveService.findAllDrivesByUser(userDto);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//
//    }
//
//    @Test
//    void testFindActiveDrives() {
//        User user = new User();
//        user.setUserId(1L);
//
//        Drive activeDrive1 = new Drive();
//        activeDrive1.setDriveId(1L);
//        activeDrive1.setDriveStatus(DriveStatus.ACTIVE);
//
//        Drive activeDrive2 = new Drive();
//        activeDrive2.setDriveId(2L);
//        activeDrive2.setDriveStatus(DriveStatus.ACTIVE);
//
//        List<Drive> activeDrives = List.of(activeDrive1, activeDrive2);
//
//        when(userMappingUtils.mapToUserEntity(userDto)).thenReturn(user);
//        when(driveRepository.findDriveByDriveStatusAndUser(user)).thenReturn(activeDrives);
//        when(driveMappingUtils.mapToDriveDto(any(Drive.class))).thenReturn(new DriveDto());
//
//        List<DriveDto> result = driveService.findActiveDrives(userDto);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//    }
}
