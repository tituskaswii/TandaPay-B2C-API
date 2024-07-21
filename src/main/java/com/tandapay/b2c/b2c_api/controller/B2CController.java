package com.tandapay.b2c.b2c_api.controller;

import com.tandapay.b2c.b2c_api.exception.BadRequestException;
import com.tandapay.b2c.b2c_api.exception.ErrorResponse;
import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.service.B2CService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class B2CController {
    private final B2CService b2cService;

    @PostMapping(value = "/b2c", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<B2CRequest> createB2CRequest(@RequestBody B2CRequest request) {
        B2CRequest response = b2cService.processB2CRequest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/b2c/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<B2CRequest> getB2CRequestStatus(@PathVariable String id) {
        B2CRequest request = b2cService.getB2CRequestStatus(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping(value = "/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<B2CRequest> updateB2CRequestStatus(@PathVariable String id, @RequestBody B2CRequest updatedRequest) {
        B2CRequest request = b2cService.updateB2CRequestStatus(id, updatedRequest.getStatus());
        return ResponseEntity.ok(request);
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
        }
    }
}
