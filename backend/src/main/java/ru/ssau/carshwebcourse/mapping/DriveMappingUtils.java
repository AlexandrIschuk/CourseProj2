package ru.ssau.carshwebcourse.mapping;

import org.springframework.stereotype.Component;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.Drive;
import ru.ssau.carshwebcourse.entity.Tariff;
import ru.ssau.carshwebcourse.entity.User;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;
import ru.ssau.carshwebcourse.repository.UserRepository;

@Component
public class DriveMappingUtils {
    private final CarRepository carRepository;

    public DriveMappingUtils(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public Drive mapToDriveEntity(DriveDto driveDto){
        Drive entity = new Drive();

        User user = new User();
        user.setUserId(driveDto.getUserId());
        entity.setDriveId(driveDto.getDriveId());
        entity.setUser(user);
        entity.setCar(driveDto.getCar());
        entity.setTariff(driveDto.getTariff());
        entity.setStartRental(driveDto.getStartRental());
        entity.setEndRental(driveDto.getEndRental());
        entity.setTotalDistance(driveDto.getTotalDistance());
        entity.setTotalAmount(driveDto.getTotalAmount());
        entity.setDriveStatus(driveDto.getDriveStatus());
        return entity;
    }

    public DriveDto mapToDriveDto(Drive entity){
        DriveDto driveDto = new DriveDto();
        driveDto.setDriveId(entity.getDriveId());
        driveDto.setUserId(entity.getUser().getUserId());
        driveDto.setCar(entity.getCar());
        driveDto.setTariff(entity.getTariff());
        driveDto.setStartRental(entity.getStartRental());
        driveDto.setEndRental(entity.getEndRental());
        driveDto.setTotalDistance(entity.getTotalDistance());
        driveDto.setTotalAmount(entity.getTotalAmount());
        driveDto.setDriveStatus(entity.getDriveStatus());
        return driveDto;
    }
}
