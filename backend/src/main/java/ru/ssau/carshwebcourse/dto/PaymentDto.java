package ru.ssau.carshwebcourse.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import ru.ssau.carshwebcourse.entity.Drive;
import ru.ssau.carshwebcourse.entity.PaymentMethod;
import ru.ssau.carshwebcourse.entity.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDto {
    private Long paymentId;
    private Drive drive;
    private Long paymentAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
}
