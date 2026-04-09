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
        Car car = new Car();
        Tariff tariff = new Tariff();
        User user = new User();
        drive.setTotalAmount(111L);
        drive.setDriveId(1L);
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
//        assertEquals(111L, newPayment.getDrive().getTotalAmount());
//    }
}
