package com.legacycorp.bikehubb.createAdvertisement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.legacycorp.bikehubb.createAdvertisement.model.BikeImage;

@Repository
public interface BikeImageRepository extends JpaRepository<BikeImage, UUID> {

    /**
     * Busca todas as imagens de uma bicicleta específica
     */
    @Query("SELECT bi FROM BikeImage bi WHERE bi.bicycle.id = :bicycleId ORDER BY bi.isPrimary DESC, bi.createdAt ASC")
    List<BikeImage> findByBicycleId(@Param("bicycleId") UUID bicycleId);

    /**
     * Busca apenas metadados das imagens (SEM carregar imageData) - Otimizado para listagem
     */
    @Query("SELECT new com.legacycorp.bikehubb.createAdvertisement.dto.BikeImageSummaryDTO(" +
           "bi.id, bi.originalFilename, bi.contentType, bi.fileSize, bi.isPrimary, bi.createdAt, " +
           "CONCAT('/api/images/', bi.id)) " +
           "FROM BikeImage bi WHERE bi.bicycle.id = :bicycleId ORDER BY bi.isPrimary DESC, bi.createdAt ASC")
    List<com.legacycorp.bikehubb.createAdvertisement.dto.BikeImageSummaryDTO> findImageSummariesByBicycleId(@Param("bicycleId") UUID bicycleId);

    /**
     * Busca a imagem principal de uma bicicleta
     */
    @Query("SELECT bi FROM BikeImage bi WHERE bi.bicycle.id = :bicycleId AND bi.isPrimary = true")
    Optional<BikeImage> findPrimaryImageByBicycleId(@Param("bicycleId") UUID bicycleId);

    /**
     * Remove todas as imagens de uma bicicleta
     */
    @Modifying
    @Query("DELETE FROM BikeImage bi WHERE bi.bicycle.id = :bicycleId")
    void deleteByBicycleId(@Param("bicycleId") UUID bicycleId);

    /**
     * Define uma imagem como principal e remove o flag das outras
     */
    @Modifying
    @Query("UPDATE BikeImage bi SET bi.isPrimary = CASE WHEN bi.id = :imageId THEN true ELSE false END WHERE bi.bicycle.id = :bicycleId")
    void setPrimaryImage(@Param("bicycleId") UUID bicycleId, @Param("imageId") UUID imageId);

    /**
     * Conta quantas imagens uma bicicleta possui
     */
    @Query("SELECT COUNT(bi) FROM BikeImage bi WHERE bi.bicycle.id = :bicycleId")
    Long countByBicycleId(@Param("bicycleId") UUID bicycleId);

    /**
     * Busca imagens por tamanho (para limpeza/otimização)
     */
    @Query("SELECT bi FROM BikeImage bi WHERE bi.fileSize > :maxSize")
    List<BikeImage> findByFileSizeGreaterThan(@Param("maxSize") Long maxSize);
}
