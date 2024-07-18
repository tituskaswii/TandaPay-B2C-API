package com.tandapay.b2c.b2c_api.controller;

import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.service.B2CService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/b2c")
@RequiredArgsConstructor
public class B2CController {
    private final B2CService b2cService;

    @PostMapping("/request")
    public ResponseEntity<B2CRequest> createB2CRequest(@Valid @RequestBody B2CRequest request) {
        B2CRequest savedRequest = b2cService.processB2CRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<B2CRequest> getB2CRequestStatus(@PathVariable String id) {
        B2CRequest request = b2cService.getB2CRequestStatus(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<B2CRequest> updateB2CRequestStatus(@PathVariable String id, @RequestBody String status) {
        B2CRequest updatedRequest = b2cService.updateB2CRequestStatus(id, status);
        return ResponseEntity.ok(updatedRequest);
    }
}
