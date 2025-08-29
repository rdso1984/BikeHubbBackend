package com.legacycorp.bikehubb.createAdvertisement.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.model.User;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle.AdvertisementStatus;

@Repository
public interface AdvertisementRepository extends JpaRepository<Bicycle, UUID> {

    // Busca básica por ID incluindo o dono do anúncio
    @Query("SELECT a FROM Advertisement a JOIN FETCH a.owner WHERE a.id = :id")
    Optional<Bicycle> findByIdWithOwner(@Param("id") UUID id);

    // Busca anúncios por status com paginação
    Page<Bicycle> findByStatus(AdvertisementStatus status, Pageable pageable);

    // Busca anúncios por usuário
    List<Bicycle> findByOwner(User owner);

    // Busca anúncios por categoria (bike ou part)
    Page<Bicycle> findByCategoryAndStatus(String category, AdvertisementStatus status, Pageable pageable);

    // Busca anúncios publicados após uma determinada data
    List<Bicycle> findByStatusAndPublishedAtAfter(AdvertisementStatus status, LocalDateTime publishedAfter);

    // Busca anúncios por intervalo de preço
    @Query("SELECT a FROM Advertisement a WHERE a.status = :status AND a.price BETWEEN :minPrice AND :maxPrice")
    Page<Bicycle> findByPriceRange(
            @Param("status") AdvertisementStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // Atualiza o status de um anúncio
    @Modifying
    @Query("UPDATE Advertisement a SET a.status = :status WHERE a.id = :id")
    int updateStatus(@Param("id") UUID id, @Param("status") AdvertisementStatus status);

    // Busca anúncios que estão pendentes de pagamento há mais de X horas
    @Query("SELECT a FROM Advertisement a WHERE a.status = 'PENDING_PAYMENT' AND a.createdAt < :threshold")
    List<Bicycle> findExpiredPendingPayments(@Param("threshold") LocalDateTime threshold);

    // Busca anúncios com filtros combinados (para busca avançada)
    @Query("SELECT a FROM Advertisement a WHERE " +
           "(:category IS NULL OR a.category = :category) AND " +
           "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
           "a.status = 'PUBLISHED'")
    Page<Bicycle> searchPublishedAdvertisements(
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // Contagem de anúncios por status
    long countByStatus(AdvertisementStatus status);

    // Verifica se um usuário é dono de um anúncio específico
    boolean existsByIdAndOwner(UUID id, User owner);
}
