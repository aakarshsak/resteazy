package com.sinha.resteazy.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestBody {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private Gender gender;
    private Role role;
}
