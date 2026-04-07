package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.carshwebcourse.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> getPaymentsByDrive_User_UserId(Long driveUserUserId);
}
