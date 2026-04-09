package ru.ssau.carshwebcourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.ssau.carshwebcourse.dto.DriveDto;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.mapping.PaymentMappingUtils;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.PaymentRepository;
import ru.ssau.carshwebcourse.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {
    private PaymentRepository paymentRepository;
    private PaymentMappingUtils paymentMappingUtils;
    private DriveService driveService;
    private PaymentService paymentService;

    private Drive testDriveDto;
    private Payment testPaymentEntity;



    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentMappingUtils = mock(PaymentMappingUtils.class);
        driveService = mock(DriveService.class);
        paymentService = new PaymentService(driveService, paymentRepository, paymentMappingUtils);


        testDriveDto = new Drive();
        testDriveDto.setDriveId(1L);
        testDriveDto.setTotalAmount(5000L);


    }

    @Test
    void testNewPayment() {
        PaymentDto inputDto = new PaymentDto();
        inputDto.setDrive(testDriveDto);

        when(paymentMappingUtils.mapToPaymentEntity(any(PaymentDto.class))).thenReturn(testPaymentEntity);
        doNothing().when(driveService).completeDrive(1L);

        PaymentDto result = paymentService.newPayment(inputDto);

        assertNotNull(result);
        Long t1 = 5000L;
        assertEquals(t1, result.getPaymentAmount());
        assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
        assertNotNull(result.getPaymentDate());
    }

    @Test
    void testGetAllPaymentToUser() {
        Long userId = 1L;

        Payment payment1 = new Payment();
        payment1.setPaymentId(1L);
        payment1.setPaymentAmount(5000L);

        Payment payment2 = new Payment();
        payment2.setPaymentId(2L);
        payment2.setPaymentAmount(3000L);

        List<Payment> payments = Arrays.asList(payment1, payment2);

        PaymentDto dto1 = new PaymentDto();
        dto1.setPaymentId(1L);
        dto1.setPaymentAmount(5000L);

        PaymentDto dto2 = new PaymentDto();
        dto2.setPaymentId(2L);
        dto2.setPaymentAmount(3000L);

        when(paymentRepository.getPaymentsByDrive_User_UserId(userId)).thenReturn(payments);
        when(paymentMappingUtils.mapToPaymentDto(payment1)).thenReturn(dto1);
        when(paymentMappingUtils.mapToPaymentDto(payment2)).thenReturn(dto2);


        List<PaymentDto> result = paymentService.getAllPaymentToUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        Long t1 = 1L;
        Long t2 = 5000L;
        Long t3 = 2L;
        Long t4 = 3000L;
        assertEquals(t1, result.get(0).getPaymentId());
        assertEquals(t2, result.get(0).getPaymentAmount());
        assertEquals(t3, result.get(1).getPaymentId());
        assertEquals(t4, result.get(1).getPaymentAmount());

        verify(paymentRepository, times(1)).getPaymentsByDrive_User_UserId(userId);
        verify(paymentMappingUtils, times(2)).mapToPaymentDto(any(Payment.class));
    }
}
