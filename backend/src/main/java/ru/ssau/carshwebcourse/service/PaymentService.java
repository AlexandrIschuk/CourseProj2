package ru.ssau.carshwebcourse.service;

import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.entity.PaymentStatus;
import ru.ssau.carshwebcourse.mapping.PaymentMappingUtils;
import ru.ssau.carshwebcourse.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMappingUtils paymentMappingUtils;
    private final DriveService driveService;

    public PaymentService(DriveService driveService,PaymentRepository paymentRepository,PaymentMappingUtils paymentMappingUtils){
        this.paymentMappingUtils = paymentMappingUtils;
        this.paymentRepository = paymentRepository;
        this.driveService = driveService;
    }

    public PaymentDto newPayment(PaymentDto paymentDto){
        paymentDto.setPaymentAmount(paymentDto.getDrive().getTotalAmount());
        paymentDto.setPaymentDate(LocalDateTime.now());
        paymentDto.setPaymentStatus(PaymentStatus.SUCCESS);
        driveService.completeDrive(paymentDto.getDrive().getDriveId());
        paymentRepository.save(paymentMappingUtils.mapToPaymentEntity(paymentDto));
        return paymentDto;
    }

    public List<PaymentDto> getAllPaymentToUser(Long userId){
        return paymentRepository.getPaymentsByDrive_User_UserId(userId).stream().map(paymentMappingUtils::mapToPaymentDto).toList();
    }
}
