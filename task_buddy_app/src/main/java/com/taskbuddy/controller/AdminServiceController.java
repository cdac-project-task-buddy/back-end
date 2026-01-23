package com.taskbuddy.controller;

import com.taskbuddy.dto.ServiceRequest;
import com.taskbuddy.entity.ServiceCategory;
import com.taskbuddy.service.ServiceCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/services")
public class AdminServiceController {

    private final ServiceCategoryService service;

    public AdminServiceController(ServiceCategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ServiceCategory create(@RequestBody ServiceRequest request) {
        return service.createService(request);
    }

    @GetMapping
    public List<ServiceCategory> getAll() {
        return service.getAllServices();
    }

    @PutMapping("/{id}")
    public ServiceCategory update(@PathVariable Long id,
                                  @RequestBody ServiceRequest request) {
        return service.updateService(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteService(id);
        return "Service deleted successfully";
    }
}
