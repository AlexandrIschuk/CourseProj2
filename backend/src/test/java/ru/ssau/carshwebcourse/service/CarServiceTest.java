package ru.ssau.carshwebcourse.service;

import org.junit.jupiter.api.Test;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.CarStatus;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.CarMappingUtils;
import ru.ssau.carshwebcourse.repository.CarRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarServiceTest {

    @Test
    void testAddCar() {
        CarRepository carRepository = mock(CarRepository.class);
        CarMappingUtils carMappingUtils = mock(CarMappingUtils.class);

        CarDto input = new CarDto();
        input.setCarBrand("BMW");

        Car entity = new Car();
        Car saved = new Car();
        CarDto output = new CarDto();

        when(carMappingUtils.mapToCarEntity(input)).thenReturn(entity);
        when(carRepository.save(entity)).thenReturn(saved);
        when(carMappingUtils.mapToCarDto(saved)).thenReturn(output);

        CarService service = new CarService(carRepository, carMappingUtils);
        CarDto result = service.addCar(input);

        assertNotNull(result);
    }

    @Test
    void testFindAllCars() {
        CarRepository carRepository = mock(CarRepository.class);
        CarMappingUtils carMappingUtils = mock(CarMappingUtils.class);

        Car car = new Car();
        when(carRepository.findAll()).thenReturn(List.of(car));
        when(carMappingUtils.mapToCarDto(car)).thenReturn(new CarDto());

        CarService service = new CarService(carRepository, carMappingUtils);
        List<CarDto> result = service.findAllCars();

        assertEquals(1, result.size());

    }

    @Test
    void testGetCarById() {
        CarRepository carRepository = mock(CarRepository.class);
        CarMappingUtils carMappingUtils = mock(CarMappingUtils.class);

        Car car = new Car();
        car.setCarId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMappingUtils.mapToCarDto(car)).thenReturn(new CarDto());

        CarService service = new CarService(carRepository, carMappingUtils);
        CarDto result = service.getCarById(1L);

        assertNotNull(result);

    }

    @Test
    void testGetCarById_NotFound() {
        CarRepository carRepository = mock(CarRepository.class);
        CarMappingUtils carMappingUtils = mock(CarMappingUtils.class);

        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        CarService service = new CarService(carRepository, carMappingUtils);

        assertThrows(NotFoundException.class, () -> {
            service.getCarById(999L);
        });
    }

    @Test
    void testUpdateCarStatus() {
        CarRepository carRepository = mock(CarRepository.class);
        CarMappingUtils carMappingUtils = mock(CarMappingUtils.class);

        Car car = new Car();
        car.setCarId(1L);
        car.setCarStatus(CarStatus.FREE);

        CarDto updateDto = new CarDto();
        updateDto.setCarStatus(CarStatus.ON_A_TRIP);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(carMappingUtils.mapToCarDto(car)).thenReturn(new CarDto());

        CarService service = new CarService(carRepository, carMappingUtils);
        CarDto result = service.updateCarStatus(1L, updateDto);

        assertNotNull(result);
        assertEquals(CarStatus.ON_A_TRIP, car.getCarStatus());

    }
}
