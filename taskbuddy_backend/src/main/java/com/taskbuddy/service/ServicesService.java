package com.taskbuddy.service;

import com.taskbuddy.dto.request.ServiceRequest;
import com.taskbuddy.dto.response.ServiceDTO;
import com.taskbuddy.entities.Services;
import com.taskbuddy.exception.ResourceNotFoundException;
import com.taskbuddy.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicesService {
    
    private final ServicesRepository servicesRepository;
    private final ModelMapper modelMapper;
    
    
    @Transactional
    public List<ServiceDTO> getAllServices() {
        return servicesRepository.findByDeletedFalse()
                .stream()
                .map(s -> modelMapper.map(s, ServiceDTO.class))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ServiceDTO getServiceById(Long id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        
        return modelMapper.map(service, ServiceDTO.class);
    }
    
    
    
    @Transactional
    public ServiceDTO createService(ServiceRequest request) {
        if (servicesRepository.existsByName(request.getName())) {
            throw new RuntimeException("Service with this name already exists");
        }
        
        Services service = modelMapper.map(request, Services.class);
        service = servicesRepository.save(service);
        
        return modelMapper.map(service, ServiceDTO.class);
    }
    
    @Transactional
    public ServiceDTO updateService(Long id, ServiceRequest request) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        
        service = servicesRepository.save(service);
        
        return modelMapper.map(service, ServiceDTO.class);
    }
    
    @Transactional
    public void deleteService(Long id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        
        service.setDeleted(true);
        servicesRepository.save(service);
    }
}