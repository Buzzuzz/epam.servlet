package com.servlet.ejournal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private final long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userType;
    private String isBlocked;
    private String sendNotification;
}
