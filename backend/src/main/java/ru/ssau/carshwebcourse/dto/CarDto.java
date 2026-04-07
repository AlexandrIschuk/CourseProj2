package ru.ssau.carshwebcourse.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import ru.ssau.carshwebcourse.entity.CarStatus;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Getter
@Setter
public class CarDto {
    private Long carId;
    private String carBrand;
    private String carModel;

    @Pattern(regexp = "^[АВЕКМНОРСТУХ]{1}\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$", message = "Введите корректный регистрационный номер!")
    private String carRegistrationNumber;

    @Min(value = 2016, message = "Автомобиль должен быть младше 10 лет!")
    @Max(value = 2026, message = "Неверный год!")
    private int year;
    private CarStatus carStatus;
}
