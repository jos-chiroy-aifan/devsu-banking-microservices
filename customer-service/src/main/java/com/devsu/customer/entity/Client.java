package com.devsu.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Person {

    @Column(name = "client_id", unique = true, nullable = false, length = 50)
    private String clientId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private Boolean status;
}