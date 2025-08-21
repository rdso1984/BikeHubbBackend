package com.legacycorp.bikehubb.createAdvertisement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.legacycorp.bikehubb.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Aqui você pode adicionar métodos específicos para o UserRepository, se necessário.
    // Por exemplo, busca por email, nome, etc.

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Exemplo de método para listar todos os usuários
    List<User> findAll();

    //Para integracao com Stripe
    Optional<User> findByStripeCustomerId(String stripeCustomerId);

}