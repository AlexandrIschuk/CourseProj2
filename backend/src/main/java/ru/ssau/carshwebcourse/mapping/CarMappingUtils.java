package ru.ssau.carshwebcourse.mapping;

import org.springframework.stereotype.Component;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.entity.Car;

@Component
public class CarMappingUtils {
    public CarDto mapToCarDto(Car entity){
        CarDto carDto = new CarDto();
        carDto.setCarId(entity.getCarId());
        carDto.setCarBrand(entity.getCarBrand());
        carDto.setCarModel(entity.getCarModel());
        carDto.setCarRegistrationNumber(entity.getCarRegistrationNumber());
        carDto.setYear(entity.getYear());
        carDto.setCarStatus(entity.getCarStatus());
        return carDto;
    }

    public Car mapToCarEntity(CarDto carDto){
        Car entity = new Car();
        entity.setCarId(carDto.getCarId());
        entity.setCarBrand(carDto.getCarBrand());
        entity.setCarModel(carDto.getCarModel());
        entity.setCarRegistrationNumber(carDto.getCarRegistrationNumber());
        entity.setYear(carDto.getYear());
        entity.setCarStatus(carDto.getCarStatus());
        return entity;
    }
}
