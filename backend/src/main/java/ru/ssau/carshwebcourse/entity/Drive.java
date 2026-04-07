package ru.ssau.carshwebcourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "drive")
public class Drive {
    @Id
    @Column(name = "drive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Column(name = "start_rental", updatable = false)
    @CurrentTimestamp
    private LocalDateTime startRental;

    @Column(name = "end_rental")
    private LocalDateTime endRental;

    @Column(name = "total_distance")
    private Long totalDistance;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Column(name = "drive_status")
    @Enumerated(EnumType.STRING)
    private DriveStatus driveStatus;

    @JsonIgnore
    @OneToOne(mappedBy = "drive", cascade = CascadeType.REMOVE)
    private Payment payment;
}
