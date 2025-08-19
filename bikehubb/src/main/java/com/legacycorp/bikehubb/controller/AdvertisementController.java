package com.legacycorp.bikehubb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    public AdvertisementController() {
        // Constructor logic if needed
    }

    @PostMapping("/create-advertisement")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
