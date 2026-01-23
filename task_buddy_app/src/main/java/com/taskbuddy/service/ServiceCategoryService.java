package com.taskbuddy.service;

import com.taskbuddy.dto.ServiceRequest;
import com.taskbuddy.entity.ServiceCategory;

import java.util.List;

public interface ServiceCategoryService {

    ServiceCategory createService(ServiceRequest request);

    List<ServiceCategory> getAllServices();

    ServiceCategory updateService(Long id, ServiceRequest request);

    void deleteService(Long id);
}
