package ru.ssau.carshwebcourse.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.DriveRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;
import ru.ssau.carshwebcourse.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CarControllerTest {

    @Autowired
    private CarController carController;
    @Autowired
    private CarRepository carRepository;

    private Long carId;



    @BeforeEach
    public void before(){
        Car car = new Car();
        car.setCarBrand("BMW");
        car.setCarStatus(CarStatus.FREE);
        Car savedCar = carRepository.save(car);
        carId = savedCar.getCarId();
        Car car1 = new Car();
        car1.setCarBrand("MB");
        car1.setCarStatus(CarStatus.ON_A_TRIP);
        carRepository.save(car1);
        Car car2 = new Car();
        car2.setCarBrand("AUDI");
        car2.setCarStatus(CarStatus.FREE);
        carRepository.save(car2);
    }

    @AfterEach
    public void after(){
        carRepository.deleteAllInBatch();
    }

    @Test
    void testFindAllCars(){
        List<CarDto> cars = carController.findAll();
        assertEquals(3, cars.size());
        assertEquals(1L, cars.get(0).getCarId());
    }

    @Test
    void testFindCarById(){
        CarDto car2 = new CarDto();
        car2.setCarBrand("LADA");
        car2.setCarStatus(CarStatus.FREE);
        CarDto newCar = carController.addCar(car2).getBody();
        CarDto car = carController.getCatById(newCar.getCarId()).getBody();
        assertEquals("LADA", car.getCarBrand());
    }
    @Test
    void testFindFreeCars(){
        List<CarDto> cars = carController.findFreeCars();
        assertEquals(2, cars.size());
        assertEquals(CarStatus.FREE, cars.get(0).getCarStatus());
        assertEquals(CarStatus.FREE, cars.get(1).getCarStatus());
    }

    @Test
    void testUpdateCarStatus(){
        CarDto carDto = new CarDto();
        carDto.setCarStatus(CarStatus.ON_A_TRIP);
        ResponseEntity<CarDto> car = carController.updateCar(carId,carDto);
        CarDto carDto1 = car.getBody();
        assertEquals(carId, carDto1.getCarId());
        assertEquals(CarStatus.ON_A_TRIP, carDto1.getCarStatus());

    }

}
