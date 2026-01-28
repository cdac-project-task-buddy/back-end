package com.taskbuddy.controller;

import com.taskbuddy.dto.request.AvailabilityRequest;
import com.taskbuddy.dto.request.WorkerProfileRequest;
import com.taskbuddy.dto.response.ApiResponse;
import com.taskbuddy.dto.response.AvailabilityDTO;
import com.taskbuddy.dto.response.WorkerDTO;
import com.taskbuddy.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkerController {
    
    private final WorkerService workerService;
    
    @GetMapping("/{id}")
    public ResponseEntity<WorkerDTO> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<WorkerDTO>> getWorkersByService(@PathVariable Long serviceId) {
        List<WorkerDTO> workers = workerService.getWorkersByService(serviceId);
        // Filter verified workers from DTOs
        List<WorkerDTO> verifiedWorkers = workers.stream()
                .filter(w -> w.getVerified() != null && w.getVerified())
                .collect(Collectors.toList());
        return ResponseEntity.ok(verifiedWorkers);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<WorkerDTO>> getTopRatedWorkers(
            @RequestParam(defaultValue = "4.0") double minRating
    ) {
        return ResponseEntity.ok(workerService.getTopRatedWorkers(minRating));
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<WorkerDTO>> getAvailableWorkers() {
        return ResponseEntity.ok(workerService.getAvailableWorkers());
    }
    
    @GetMapping
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerDTO> updateProfile(
            @Valid @RequestBody WorkerProfileRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(workerService.updateWorkerProfile(request, authentication.getName()));
    }
    
    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAuthority('ROLE_WORKER')")
    public ResponseEntity<ApiResponse<Void>> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available
    ) {
        workerService.updateAvailability(id, available);

        return ResponseEntity.ok(
            new ApiResponse<>(
                available
                    ? "Availability set to AVAILABLE"
                    : "Availability set to UNAVAILABLE",
                null
            )
        );
    }
    
    @PostMapping("/{id}/availability")
    @PreAuthorize("hasAuthority('ROLE_WORKER')")
    public ResponseEntity<ApiResponse<AvailabilityDTO>> addAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequest request
    ) {
        AvailabilityDTO dto = workerService.addAvailability(id, request);

        return ResponseEntity.ok(
            new ApiResponse<>(
                "Availability updated successfully",
                dto
            )
        );
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<List<AvailabilityDTO>> getWorkerAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerAvailability(id));
    }
}
