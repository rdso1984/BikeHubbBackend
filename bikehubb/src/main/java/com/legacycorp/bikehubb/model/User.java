package com.legacycorp.bikehubb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;
    
    // @Column(nullable = false)
    // private String password;

    @Column(nullable = false)
    private String nome;

    private String phone;

    // @Column(nullable = false)
    // private boolean active = true;
    
    // Campo para mapear UUID do JWT/autenticação externa
    @Column(unique = true)
    private String externalId; // UUID do sistema de autenticação

    // Relacionamento com Advertisement removido temporariamente
    // pois o owner agora é um UUID simples, não uma entidade
    // @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Advertisement> advertisements = new ArrayList<Advertisement>();

    //Campo para integracao com Stripe (opcional)
    private String stripeCustomerId;

}
