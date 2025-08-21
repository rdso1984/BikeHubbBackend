package com.legacycorp.bikehubb.createAdvertisement.controller;

import com.legacycorp.bikehubb.createAdvertisement.dto.AdvertisementRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @PostMapping("/create-advertisement")
    public ResponseEntity<Map<String, String>> createAdvertisement(
        AdvertisementRequest request,
        @RequestParam(value = "images", required = false) MultipartFile[] images){
        //TODO: process POST request
        System.out.println("Chegou na API");
        return ResponseEntity.ok(Map.of("message", "Anúncio criado com sucesso"));
    }
    
}
