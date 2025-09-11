package com.legacycorp.bikehubb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "https://bikehubb.netlify.app",
    "https://bikehubbbackend.onrender.com",
    "http://localhost:3000",
    "http://localhost:3001",
    "http://localhost:4200",
    "http://localhost:4201"
}, allowCredentials = "true")
public class InfoController {

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "BikeHub Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("port", serverPort);
        response.put("profile", System.getProperty("spring.profiles.active", "default"));
        response.put("message", "Backend está funcionando!");
        
        // Informações para o frontend identificar a URL correta
        response.put("baseUrl", "https://bikehubbbackend.onrender.com"); 
        response.put("corsEnabled", true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("apiBaseUrl", "https://bikehubbbackend.onrender.com/api");
        config.put("environment", "production");
        config.put("corsEnabled", "true");
        return ResponseEntity.ok(config);
    }
}
