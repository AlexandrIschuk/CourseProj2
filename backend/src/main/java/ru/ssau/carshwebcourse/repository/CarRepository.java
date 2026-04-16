package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.CarStatus;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    List<Car> findCarByCarStatus(CarStatus carStatus);
}
