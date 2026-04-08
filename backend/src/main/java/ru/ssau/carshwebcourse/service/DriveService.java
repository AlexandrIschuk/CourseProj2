package ru.ssau.carshwebcourse.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.exceptionHandler.CarIsNotFreeException;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.DriveMappingUtils;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.DriveRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriveService {
    private final DriveMappingUtils driveMappingUtils;
    private final UserMappingUtils userMappingUtils;
    private final DriveRepository driveRepository;
    private final TariffRepository tariffRepository;
    private final CarRepository carRepository;

    public DriveService(CarRepository carRepository,DriveMappingUtils driveMappingUtils, UserMappingUtils userMappingUtils, DriveRepository driveRepository, TariffRepository tariffRepository){
        this.driveMappingUtils = driveMappingUtils;
        this.driveRepository = driveRepository;
        this.tariffRepository = tariffRepository;
        this.userMappingUtils = userMappingUtils;
        this.carRepository = carRepository;
    }
    public DriveDto startDrive(DriveDto drive){
        Long count = driveRepository.countDrivesByDriveStatusAndUser_UserId(DriveStatus.ACTIVE,drive.getUserId());
        if(count == 1){
            throw new IllegalStateException("Нельзя начать больше 1 поездки");
        }
        Drive drive1 = driveMappingUtils.mapToDriveEntity(drive);
        Car car = carRepository.findById(drive1.getCar().getCarId()).orElseThrow(() -> new NotFoundException("Car not found"));
        if(!car.getCarStatus().equals(CarStatus.FREE)){
            throw new CarIsNotFreeException("Машина занята!");
        }
        car.setCarStatus(CarStatus.ON_A_TRIP);
        drive1.setCar(car);
        return driveMappingUtils.mapToDriveDto(driveRepository.save(driveMappingUtils.mapToDriveEntity(drive)));
    }

    public Optional<DriveDto> findDriveById(Long id){
        return Optional.ofNullable(driveMappingUtils.mapToDriveDto(driveRepository.getDriveByDriveId(id)));
    }

    public List<DriveDto> findAllDrives(){
        List<Drive> drives = driveRepository.findAll();
        return drives.stream()
                .map(driveMappingUtils::mapToDriveDto).collect(Collectors.toList());
    }

    public List<DriveDto> findAllDrivesByUser(UserDto user){
        List<Drive> drives = driveRepository.findDriveHistoryByUser(userMappingUtils.mapToUserEntity(user));
        return drives.stream()
                .map(driveMappingUtils::mapToDriveDto).collect(Collectors.toList());
    }

    public List<DriveDto> findActiveDrives(UserDto user){
        List<Drive> drives = driveRepository.findDriveByDriveStatusAndUser(userMappingUtils.mapToUserEntity(user));
        return drives.stream()
                .map(driveMappingUtils::mapToDriveDto).collect(Collectors.toList());
    }

    public DriveDto updateDriveStatus(Long id, DriveDto drive){
        Drive drive1 = driveRepository.findById(id).orElseThrow();
        drive1.setDriveStatus(drive.getDriveStatus());
        Tariff tariff = drive1.getTariff();
        if(drive.getDriveStatus() == DriveStatus.PENDING_PAYMENT){
            drive1.setEndRental(LocalDateTime.now());
            if (drive1.getTariff().getTariffType() == TariffType.KILOMETER){
                drive1.setTotalDistance(drive.getTotalDistance());
                drive1.setTotalAmount(drive.getTotalDistance()*tariff.getPrice());
            }
            else {
                long minutes = Duration.between(drive1.getStartRental(),drive1.getEndRental()).toMinutes();
                drive1.setTotalDistance(minutes);
                drive1.setTotalAmount(minutes * tariff.getPrice());
            }
        }
        driveRepository.save(drive1);
        return driveMappingUtils.mapToDriveDto(drive1);
    }
    public void completeDrive(Long id){
        Drive drive1 = driveRepository.findById(id).orElseThrow();
        drive1.setDriveStatus(DriveStatus.COMPLETED);
        drive1.getCar().setCarStatus(CarStatus.FREE);
        driveRepository.save(drive1);
        //return driveMappingUtils.mapToDriveDto(drive1);
    }

    public DriveDto cancelDrive(Long id){
        Drive drive1 = driveRepository.findById(id).orElseThrow();
        if(drive1.getStartRental().isBefore(LocalDateTime.now().minusMinutes(5))){
            throw new IllegalStateException("The drive was start more 5 minutes!");
        }
        drive1.setDriveStatus(DriveStatus.CANCELLED);
        drive1.getCar().setCarStatus(CarStatus.FREE);
        drive1.setEndRental(LocalDateTime.now());
        driveRepository.save(drive1);
        return driveMappingUtils.mapToDriveDto(drive1);
    }

    public void deleteDrive(Long id){
        driveRepository.deleteById(id);
    }
}
