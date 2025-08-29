package com.legacycorp.bikehubb.createAdvertisement.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;

public interface AdvertisementStatsRepository extends Repository<Bicycle, UUID> {

    @Query("SELECT new map(a.category as category, COUNT(a) as count) " +
           "FROM Bicycle a " +
           "WHERE a.status = 'PUBLISHED' " +
           "GROUP BY a.category")
    List<Map<String, Object>> countPublishedAdsByCategory();

    @Query("SELECT new map(FUNCTION('DATE', a.publishedAt) as date, COUNT(a) as count) " +
           "FROM Bicycle a " +
           "WHERE a.status = 'PUBLISHED' AND a.publishedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY FUNCTION('DATE', a.publishedAt)")
    List<Map<String, Object>> countPublishedAdsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(a.price) FROM Bicycle a WHERE a.status = 'PUBLISHED' AND a.category = :category")
    BigDecimal averagePriceByCategory(@Param("category") String category);
}
