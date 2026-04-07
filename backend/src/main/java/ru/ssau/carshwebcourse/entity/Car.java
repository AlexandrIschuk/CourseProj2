package ru.ssau.carshwebcourse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @Column(name = "car_brand")
    private String carBrand;

    @Column(name = "car_model")
    private String carModel;

    @Column(name = "car_registration_number", unique = true)
    private String carRegistrationNumber;

    @Column(name = "year")
    private int year;

    @Column(name = "car_status")
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;
}
