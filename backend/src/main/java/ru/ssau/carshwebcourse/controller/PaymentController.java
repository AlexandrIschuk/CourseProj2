package ru.ssau.carshwebcourse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.carshwebcourse.dto.PaymentDto;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;
import ru.ssau.carshwebcourse.service.PaymentService;

import java.util.List;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final CustomUserDetailsService userDetailsService;

    public PaymentController(PaymentService paymentService,CustomUserDetailsService userDetailsService){
        this.paymentService = paymentService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<PaymentDto> newPayment(@RequestBody PaymentDto payment){
        return new ResponseEntity<>(paymentService.newPayment(payment), HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public List<PaymentDto> findAll(@AuthenticationPrincipal UserDetails user){
       return paymentService.getAllPaymentToUser(userDetailsService.getUserByEmail(user.getUsername()).getUserId());
    }
}
