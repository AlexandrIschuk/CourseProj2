package ru.ssau.carshwebcourse.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.DriveStatus;
import ru.ssau.carshwebcourse.entity.Tariff;
import ru.ssau.carshwebcourse.entity.User;

import java.time.LocalDateTime;
@Getter
@Setter
public class DriveDto {
    private Long driveId;
    private Long userId;
    private Car car;
    private Tariff tariff;
    private LocalDateTime startRental;
    private LocalDateTime endRental;
    private Long totalDistance;
    private Long totalAmount;
    private DriveStatus driveStatus;
}
