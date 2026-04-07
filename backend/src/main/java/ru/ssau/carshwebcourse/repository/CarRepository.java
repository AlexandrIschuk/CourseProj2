package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.CarStatus;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    Car getCarByCarId(Long carId);

    List<Car> findCarByCarStatus(CarStatus carStatus);
}
