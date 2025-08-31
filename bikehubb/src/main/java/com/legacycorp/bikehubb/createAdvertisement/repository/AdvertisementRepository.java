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
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle.AdvertisementStatus;

@Repository
public interface AdvertisementRepository extends JpaRepository<Bicycle, UUID> {

    // Busca básica por ID (sem JOIN pois owner agora é Long referenciando users.id)
    Optional<Bicycle> findById(UUID id);

    // Busca anúncios por status com paginação
    Page<Bicycle> findByStatus(AdvertisementStatus status, Pageable pageable);

    // Busca anúncios por usuário (usando Long do owner referenciando users.id)
    List<Bicycle> findByOwner(Long owner);

    // Busca anúncios por categoria (bike ou part)
    Page<Bicycle> findByCategoryAndStatus(String category, AdvertisementStatus status, Pageable pageable);

    // Busca anúncios publicados após uma determinada data
    List<Bicycle> findByStatusAndPublishedAtAfter(AdvertisementStatus status, LocalDateTime publishedAfter);

    // Busca anúncios por intervalo de preço
    @Query("SELECT b FROM Bicycle b WHERE b.status = :status AND b.price BETWEEN :minPrice AND :maxPrice")
    Page<Bicycle> findByPriceRange(
            @Param("status") AdvertisementStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // Atualiza o status de um anúncio
    @Modifying
    @Query("UPDATE Bicycle b SET b.status = :status WHERE b.id = :id")
    int updateStatus(@Param("id") UUID id, @Param("status") AdvertisementStatus status);

    // Busca anúncios que estão pendentes de pagamento há mais de X horas
    @Query("SELECT b FROM Bicycle b WHERE b.status = 'PENDING_PAYMENT' AND b.createdAt < :threshold")
    List<Bicycle> findExpiredPendingPayments(@Param("threshold") LocalDateTime threshold);

    // Busca anúncios com filtros combinados (para busca avançada)
    @Query("SELECT b FROM Bicycle b WHERE " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
           "b.status = 'PUBLISHED'")
    Page<Bicycle> searchPublishedAdvertisements(
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // Contagem de anúncios por status
    long countByStatus(AdvertisementStatus status);

    // Verifica se um usuário é dono de um anúncio específico (usando UUID)
    boolean existsByIdAndOwner(UUID id, Long owner);

    // Busca anúncios expirados que ainda não foram marcados como EXPIRED
    @Query("SELECT b FROM Bicycle b WHERE b.expiresAt < :currentTime AND b.status != 'EXPIRED'")
    List<Bicycle> findExpiredAdvertisements(@Param("currentTime") LocalDateTime currentTime);

    // Busca anúncios que expiram em breve (nos próximos X dias)
    @Query("SELECT b FROM Bicycle b WHERE b.expiresAt BETWEEN :currentTime AND :futureTime AND b.status = 'PUBLISHED'")
    List<Bicycle> findAdvertisementsExpiringInDays(
            @Param("currentTime") LocalDateTime currentTime,
            @Param("futureTime") LocalDateTime futureTime);

    // Busca anúncios com múltiplos filtros aplicados - com FETCH das imagens
    @Query("SELECT DISTINCT b FROM Bicycle b LEFT JOIN FETCH b.images WHERE " +
           "(b.status = 'PUBLISHED' OR b.status = 'DRAFT') AND " +
           "(:state IS NULL OR LOWER(b.state) LIKE LOWER(CONCAT('%', :state, '%'))) AND " +
           "(:city IS NULL OR LOWER(b.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:neighborhood IS NULL OR LOWER(b.neighborhood) LIKE LOWER(CONCAT('%', :neighborhood, '%'))) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
           "(:condition IS NULL OR LOWER(b.condition) = LOWER(:condition)) AND " +
           "(:category IS NULL OR LOWER(b.category) = LOWER(:category)) AND " +
           "(:brand IS NULL OR LOWER(b.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
           "ORDER BY " +
           "CASE WHEN :sort = 'newest' THEN b.createdAt END DESC, " +
           "CASE WHEN :sort = 'oldest' THEN b.createdAt END ASC, " +
           "CASE WHEN :sort = 'price_asc' THEN b.price END ASC, " +
           "CASE WHEN :sort = 'price_desc' THEN b.price END DESC, " +
           "CASE WHEN :sort = 'title_asc' THEN b.title END ASC, " +
           "CASE WHEN :sort = 'title_desc' THEN b.title END DESC, " +
           "b.createdAt DESC")
    List<Bicycle> findAdvertisementsWithFilters(
            @Param("state") String state,
            @Param("city") String city,
            @Param("neighborhood") String neighborhood,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("condition") String condition,
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("sort") String sort);
            
    // Método simples para teste - busca todos os anúncios sem filtros COM imagens
    @Query("SELECT DISTINCT b FROM Bicycle b LEFT JOIN FETCH b.images ORDER BY b.createdAt DESC")
    List<Bicycle> findAllForTesting();
}
