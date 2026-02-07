package com.taskbuddy.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long bookingId;
    private BigDecimal amount;
    private String currency = "INR";
}