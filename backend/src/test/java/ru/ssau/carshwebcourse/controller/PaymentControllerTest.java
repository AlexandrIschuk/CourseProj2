package ru.ssau.carshwebcourse.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.repository.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private DriveRepository driveRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentController paymentController;


    private Drive drive;

    @BeforeEach
    public void before(){
        drive = new Drive();
        User user = new User();
        userRepository.save(user);
        Tariff tariff = new Tariff();
        tariffRepository.save(tariff);
        Car car = new Car();
        carRepository.save(car);
        drive.setCar(car);
        drive.setTariff(tariff);
        drive.setUser(user);
        drive.setTotalAmount(100L);
        driveRepository.save(drive);
    }

    @AfterEach
    public void after(){
        paymentRepository.deleteAllInBatch();
    }

//    @Test
//    void newPaymentTest(){
//        PaymentDto paymentDto = new PaymentDto();
//        paymentDto.setDrive(drive);
//        PaymentDto newPayment = paymentController.newPayment(paymentDto).getBody();
//        assertEquals(drive, newPayment.getDrive());
//    }
}
