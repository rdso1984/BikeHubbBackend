package com.legacycorp.bikehubb.model;

import java.util.ArrayList;
import java.util.List;

import com.legacycorp.bikehubb.createAdvertisement.model.Advertisement;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false)
    private boolean active = true;
    
    // Campo para mapear UUID do JWT/autenticação externa
    @Column(unique = true)
    private String externalId; // UUID do sistema de autenticação

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Advertisement> advertisements = new ArrayList<Advertisement>();

    //Campo para integracao com Stripe (opcional)
    private String stripeCustomerId;

}
