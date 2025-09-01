package com.legacycorp.bikehubb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://*.netlify.app", "http://localhost:3000", "http://localhost:3001"})
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "BikeHub Backend");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-cors")
    public ResponseEntity<Map<String, String>> testCors() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "CORS está funcionando!");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test-post")
    public ResponseEntity<Map<String, String>> testPost(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "POST request funcionando!");
        response.put("received", body != null ? body.toString() : "No body");
        return ResponseEntity.ok(response);
    }
}
