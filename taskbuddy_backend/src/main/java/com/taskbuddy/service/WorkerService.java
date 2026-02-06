package com.taskbuddy.service;

import com.taskbuddy.dto.request.AvailabilityRequest;
import com.taskbuddy.dto.request.WorkerProfileRequest;
import com.taskbuddy.dto.response.AddressDTO;
import com.taskbuddy.dto.response.AvailabilityDTO;
import com.taskbuddy.dto.response.ReviewDTO;
import com.taskbuddy.dto.response.ServiceDTO;
import com.taskbuddy.dto.response.WorkerDTO;
import com.taskbuddy.entities.*;
import com.taskbuddy.exception.ResourceNotFoundException;
import com.taskbuddy.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService {
    
    private final WorkerRepository workerRepository;
    private final ServicesRepository servicesRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
  
    private final AvailabilityRepository availabilityRepository;
    private final ModelMapper modelMapper;
    
    
    
    @Transactional
    public List<WorkerDTO> getAllWorkers() {
        return workerRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToWorkerDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<WorkerDTO> getAllWorkerswithServices() {
        return workerRepository.findAllWithServices()
            .stream()
            .map(this::mapToWorkerDTO)
            .toList();
    }
    @Transactional
    public WorkerDTO getWorkerById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        
        return mapToWorkerDTO(worker);
    }
    
    public void verifyWorker(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        worker.setVerified(true);
        workerRepository.save(worker);
    }

    
    @Transactional
    public List<WorkerDTO> getWorkersByService(Long serviceId) {
    	List<Worker> workers = workerRepository.findByServiceId(serviceId);
    	if (workers.isEmpty()) {
            throw new ResourceNotFoundException(
                "No workers found for service id: " + serviceId
            );
        }
        return workers
                .stream()
                .map(this::mapToWorkerDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<WorkerDTO> getTopRatedWorkers(double minRating) {
    	List<Worker> workers = workerRepository.findByMinRating(minRating);
    	if (workers.isEmpty()) {
            throw new ResourceNotFoundException(
                "No workers found for this rating"
            );
        }
        return workers
                .stream()
                .map(this::mapToWorkerDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<WorkerDTO> getAvailableWorkers() {
        return workerRepository.findAvailableWorkers()
                .stream()
                .map(this::mapToWorkerDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public WorkerDTO updateWorkerProfile(WorkerProfileRequest request, String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Worker worker = workerRepository.findByUserDetailsId(user.getId())
                .orElseThrow(() -> new RuntimeException("Worker profile not found"));
        
        worker.setExperienceInYears(request.getExperienceInYears());
        worker.setFees(request.getFees());
        
        // Update services
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            List<Services> services = servicesRepository.findAllById(request.getServiceIds());
            worker.setServices(services);
        }
        
        // Update address
        if (request.getAddress() != null) {
            Address address = modelMapper.map(request.getAddress(), Address.class);
            address = addressRepository.save(address);
            worker.setAddress(address);
        }
        
        worker = workerRepository.save(worker);
        
        return mapToWorkerDTO(worker);
    }
    
    @Transactional
    public void updateAvailability(Long workerId, boolean available) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        
        worker.setAvailablity(available);
        workerRepository.save(worker);
    }
    
    @Transactional
    public AvailabilityDTO addAvailability(Long workerId, AvailabilityRequest request) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        
        Availability availability = new Availability();
        availability.setWorker(worker);
        availability.setDay(request.getDay());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        
        availability = availabilityRepository.save(availability);
        
        return modelMapper.map(availability, AvailabilityDTO.class);
    }
    
    public List<AvailabilityDTO> getWorkerAvailability(Long workerId) {
        return availabilityRepository.findByWorkerId(workerId)
                .stream()
                .map(a -> modelMapper.map(a, AvailabilityDTO.class))
                .collect(Collectors.toList());
    }
    
    private WorkerDTO mapToWorkerDTO(Worker worker) {
        WorkerDTO dto = WorkerDTO.builder()
                .id(worker.getId())
                .experienceInYears(worker.getExperienceInYears())
                .fees(worker.getFees())
                .rating(worker.getRating())
                .availablity(worker.isAvailablity())
                .verified(worker.getVerified())  
                .build();
        
        if (worker.getUserDetails() != null) {
            User user = worker.getUserDetails();
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
        }
        
        if (worker.getAddress() != null) {
            dto.setAddress(modelMapper.map(worker.getAddress(), AddressDTO.class));
        }
        
        if (worker.getServices() != null && !worker.getServices().isEmpty()) {
            dto.setServices(worker.getServices().stream()
                    .map(s -> modelMapper.map(s, ServiceDTO.class))
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }



}

