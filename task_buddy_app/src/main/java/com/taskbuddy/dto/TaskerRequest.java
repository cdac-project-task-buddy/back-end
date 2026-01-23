package com.taskbuddy.dto;

import lombok.Data;

@Data
public class TaskerRequest {
    private String name;
    private String email;
    private String password;
    private String skills;
}
