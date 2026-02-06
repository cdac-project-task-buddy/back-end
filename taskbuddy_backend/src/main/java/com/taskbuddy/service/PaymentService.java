package com.taskbuddy.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.taskbuddy.dto.request.PaymentRequest;
import com.taskbuddy.dto.request.PaymentVerificationRequest;
import com.taskbuddy.dto.response.PaymentOrderResponse;
import com.taskbuddy.entities.Booking;
import com.taskbuddy.entities.Payment;
import com.taskbuddy.entities.PaymentStatus;
import com.taskbuddy.entities.Status;
import com.taskbuddy.exception.BadRequestException;
import com.taskbuddy.exception.ResourceNotFoundException;
import com.taskbuddy.repository.BookingRepository;
import com.taskbuddy.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    @Transactional
    public PaymentOrderResponse createPaymentOrder(PaymentRequest request) throws RazorpayException {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", request.getAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue());
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", "booking_" + booking.getId());
        
        Order order = razorpay.orders.create(orderRequest);
        
        // Save payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        
        paymentRepository.save(payment);
        
        return new PaymentOrderResponse(
            order.get("id"),
            request.getAmount(),
            request.getCurrency(),
            razorpayKeyId
        );
    }
    
    @Transactional
    public boolean verifyPayment(PaymentVerificationRequest request) throws RazorpayException {
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        // Verify signature
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());
        
        boolean isValidSignature = Utils.verifyPaymentSignature(options, razorpayKeySecret);
        
        if (isValidSignature) {
            // Update payment status
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);
            
            // Update booking status to PENDING (waiting for provider acceptance)
            Booking booking = payment.getBooking();
            booking.setStatus(Status.PENDING);
            bookingRepository.save(booking);
            
            return true;
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            
            // Cancel the booking if payment failed
            Booking booking = payment.getBooking();
            booking.setStatus(Status.CANCELLED);
            bookingRepository.save(booking);
            
            throw new BadRequestException("Payment verification failed");
        }
    }
    
    @Transactional
    public boolean processRefund(Long bookingId) throws RazorpayException {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BadRequestException("Cannot refund incomplete payment");
        }
        
        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        
        // Create refund
        JSONObject refundRequest = new JSONObject();
        refundRequest.put("amount", payment.getAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue());
        refundRequest.put("speed", "normal");
        
        try {
            razorpay.payments.refund(payment.getRazorpayPaymentId(), refundRequest);
            
            // Update payment status
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
            
            return true;
        } catch (RazorpayException e) {
            throw new BadRequestException("Refund failed: " + e.getMessage());
        }
    }
}