package ru.ssau.carshwebcourse.service;

import lombok.Setter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.CarMappingUtils;
import ru.ssau.carshwebcourse.repository.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CarMappingUtils carMappingUtils;
    public CarService(CarRepository carRepository, CarMappingUtils carMappingUtils){
        this.carRepository = carRepository;
        this.carMappingUtils = carMappingUtils;
    }
    public CarDto addCar(CarDto car){
        return carMappingUtils.mapToCarDto(carRepository.save(carMappingUtils.mapToCarEntity(car)));
    }

    public List<CarDto> findAllCars(){
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(carMappingUtils::mapToCarDto).collect(Collectors.toList());
    }

    public CarDto getCarById(Long id){
        Car entity = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found"));
        return carMappingUtils.mapToCarDto(entity);
    }

    public List<CarDto> findFreeCars(){
        List<Car> cars = carRepository.findCarByCarStatus(CarStatus.FREE);
        return cars.stream().map(carMappingUtils::mapToCarDto).collect(Collectors.toList());
    }

    public CarDto updateCarStatus(Long id, CarDto car){
        Car car1 = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found"));
        car1.setCarStatus(car.getCarStatus());
        carRepository.save(car1);
        return carMappingUtils.mapToCarDto(car1);
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }
}
