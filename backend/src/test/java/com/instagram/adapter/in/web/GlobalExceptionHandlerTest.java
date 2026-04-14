package com.instagram.adapter.in.web;

import com.instagram.adapter.in.web.dto.ApiResponse;
import com.instagram.domain.exception.PostNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handlePostNotFound_Returns404() {
        PostNotFoundException ex = new PostNotFoundException(UUID.randomUUID());
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handlePostNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Post not found with id: " + ex.getPostId(), response.getBody().error());
    }

    @Test
    void handleEntityNotFound_Returns404() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity missing");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleEntityNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Entity missing", response.getBody().error());
    }

    @Test
    void handleValidation_Returns400WithErrors() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "field", "must not be blank"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("must not be blank", response.getBody().error());
    }

    @Test
    void handleConstraintViolation_Returns400() {
        ConstraintViolationException ex = new ConstraintViolationException("Violated", null);
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().error());
    }

    @Test
    void handleHttpMessageNotReadable_Returns400() {
        @SuppressWarnings("deprecation")
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Malformed JSON");

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleHttpMessageNotReadable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Malformed request body", response.getBody().error());
    }

    @Test
    void handleException_Returns500() {
        Exception ex = new Exception("Super bad error");
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().error());
    }
}
