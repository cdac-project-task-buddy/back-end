package com.taskbuddy.controller;

import com.taskbuddy.dto.request.ServiceRequest;
import com.taskbuddy.dto.response.ServiceDTO;
import com.taskbuddy.service.ServicesService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServicesController {
private final ServicesService servicesService;

@GetMapping
public ResponseEntity<List<ServiceDTO>> getAllServices() {
    return ResponseEntity.status(HttpStatus.CREATED).body(servicesService.getAllServices());
}

@GetMapping("/{id}")
public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
    return ResponseEntity.ok(servicesService.getServiceById(id));
}

@PostMapping
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceRequest request) {
    return ResponseEntity.ok(servicesService.createService(request));
}

@PutMapping("/{id}")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public ResponseEntity<ServiceDTO> updateService(
        @PathVariable Long id,
        @Valid @RequestBody ServiceRequest request
) {
    return ResponseEntity.ok(servicesService.updateService(id, request));
}

@DeleteMapping("/{id}")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")

public ResponseEntity<?> deleteService(@PathVariable Long id) {
    servicesService.deleteService(id);
    return ResponseEntity.ok(
            Map.of("message", "Service deleted successfully")
        );
}
}