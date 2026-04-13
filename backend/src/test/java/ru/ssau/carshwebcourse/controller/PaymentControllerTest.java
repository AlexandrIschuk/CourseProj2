package ru.ssau.carshwebcourse.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class PaymentControllerTest {
    @Autowired
    private PaymentController paymentController;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private UserRepository userRepository;

    private Drive testDrive;

    private UserDetails userDet;
    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    public void before() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        User savedUser = userRepository.save(user);
        Set<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        userDet = new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),roles);
        Car car = new Car();
        car.setCarBrand("BMW");
        car.setCarStatus(CarStatus.FREE);
        Car savedCar = carRepository.save(car);

        Drive drive = new Drive();
        drive.setUser(savedUser);
        drive.setCar(savedCar);
        drive.setDriveStatus(DriveStatus.COMPLETED);
        drive.setTotalAmount(5000L);
        testDrive = driveRepository.save(drive);
    }

    @AfterEach
    public void after() {
        paymentRepository.deleteAllInBatch();
        driveRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void testNewPayment() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setDrive(testDrive);

        PaymentDto result = paymentController.newPayment(paymentDto).getBody();

        assertNotNull(result);
        assertEquals(5000L, result.getPaymentAmount());
        assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
    }

    @Test
    void testFindAll() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setDrive(testDrive);
        paymentController.newPayment(paymentDto);

        List<PaymentDto> payments = paymentController.findAll(userDet);

        assertEquals(1, payments.size());
        assertEquals(5000L, payments.get(0).getPaymentAmount());
    }
}
