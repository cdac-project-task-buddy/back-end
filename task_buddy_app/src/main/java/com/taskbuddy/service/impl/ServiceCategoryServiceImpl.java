package com.taskbuddy.service.impl;

import com.taskbuddy.dto.ServiceRequest;
import com.taskbuddy.entity.ServiceCategory;
import com.taskbuddy.repository.ServiceCategoryRepository;
import com.taskbuddy.service.ServiceCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryServiceImpl(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceCategory createService(ServiceRequest request) {
        ServiceCategory service = new ServiceCategory();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        return repository.save(service);
    }

    @Override
    public List<ServiceCategory> getAllServices() {
        return repository.findAll();
    }

    @Override
    public ServiceCategory updateService(Long id, ServiceRequest request) {
        ServiceCategory service = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());

        return repository.save(service);
    }

    @Override
    public void deleteService(Long id) {
        repository.deleteById(id);
    }
}
