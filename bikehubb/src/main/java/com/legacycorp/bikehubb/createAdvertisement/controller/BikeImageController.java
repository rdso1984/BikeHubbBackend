package com.legacycorp.bikehubb.createAdvertisement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legacycorp.bikehubb.createAdvertisement.model.BikeImage;
import com.legacycorp.bikehubb.createAdvertisement.repository.BikeImageRepository;

@RestController
@RequestMapping("/api/images")
public class BikeImageController {

    @Autowired
    private BikeImageRepository bikeImageRepository;

    /**
     * Busca uma imagem específica por ID
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID imageId) {
        try {
            BikeImage bikeImage = bikeImageRepository.findById(imageId)
                .orElse(null);
            
            if (bikeImage == null) {
                return ResponseEntity.notFound().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(bikeImage.getContentType()));
            headers.setContentLength(bikeImage.getFileSize());
            headers.set("Content-Disposition", "inline; filename=\"" + bikeImage.getOriginalFilename() + "\"");
            
            return new ResponseEntity<>(bikeImage.getImageData(), headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar imagem " + imageId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca todas as imagens de uma bicicleta
     */
    @GetMapping("/bicycle/{bicycleId}")
    public ResponseEntity<List<BikeImageInfo>> getBicycleImages(@PathVariable UUID bicycleId) {
        try {
            List<BikeImage> images = bikeImageRepository.findByBicycleId(bicycleId);
            
            List<BikeImageInfo> imageInfos = images.stream()
                .map(img -> new BikeImageInfo(
                    img.getId(),
                    img.getOriginalFilename(),
                    img.getContentType(),
                    img.getFileSize(),
                    img.getFormattedSize(),
                    img.isPrimary(),
                    img.getCreatedAt()
                ))
                .toList();
            
            return ResponseEntity.ok(imageInfos);
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar imagens da bicicleta " + bicycleId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca apenas a imagem principal de uma bicicleta
     */
    @GetMapping("/bicycle/{bicycleId}/primary")
    public ResponseEntity<byte[]> getPrimaryImage(@PathVariable UUID bicycleId) {
        try {
            BikeImage primaryImage = bikeImageRepository.findPrimaryImageByBicycleId(bicycleId)
                .orElse(null);
            
            if (primaryImage == null) {
                return ResponseEntity.notFound().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(primaryImage.getContentType()));
            headers.setContentLength(primaryImage.getFileSize());
            headers.set("Content-Disposition", "inline; filename=\"" + primaryImage.getOriginalFilename() + "\"");
            
            return new ResponseEntity<>(primaryImage.getImageData(), headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar imagem principal da bicicleta " + bicycleId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DTO para informações da imagem (sem os dados binários)
     */
    public static record BikeImageInfo(
        UUID id,
        String originalFilename,
        String contentType,
        Long fileSize,
        String formattedSize,
        boolean isPrimary,
        java.time.LocalDateTime createdAt
    ) {}
}
