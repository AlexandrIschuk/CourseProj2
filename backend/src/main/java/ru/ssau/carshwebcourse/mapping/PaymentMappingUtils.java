package ru.ssau.carshwebcourse.mapping;

import org.springframework.stereotype.Component;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.entity.Payment;

@Component
public class PaymentMappingUtils {
    public Payment mapToPaymentEntity(PaymentDto payment){
        Payment entity = new Payment();
        entity.setPaymentId(payment.getPaymentId());
        entity.setDrive(payment.getDrive());
        entity.setPaymentAmount(payment.getPaymentAmount());
        entity.setPaymentMethod(payment.getPaymentMethod());
        entity.setPaymentDate(payment.getPaymentDate());
        entity.setPaymentStatus(payment.getPaymentStatus());
        return entity;
    }

    public PaymentDto mapToPaymentDto(Payment entity){
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(entity.getPaymentId());
        paymentDto.setDrive(entity.getDrive());
        paymentDto.setPaymentAmount(entity.getPaymentAmount());
        paymentDto.setPaymentMethod(entity.getPaymentMethod());
        paymentDto.setPaymentDate(entity.getPaymentDate());
        paymentDto.setPaymentStatus(entity.getPaymentStatus());
        return paymentDto;
    }
}
