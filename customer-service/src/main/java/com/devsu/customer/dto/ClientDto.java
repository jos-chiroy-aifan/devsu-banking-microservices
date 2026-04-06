package com.devsu.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long personId;

    @NotBlank(message = "Name is required")
    private String name;

    private String gender;

    private Integer age;

    @NotBlank(message = "Identification is required")
    private String identification;

    private String address;

    private String phone;

    private String clientId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Status is required")
    private Boolean status;
}