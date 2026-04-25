package com.instagram.adapter.in.web;

import org.springframework.web.bind.annotation.RestController;

import com.instagram.adapter.in.web.dto.request.UploadUrlRequest;
import com.instagram.adapter.in.web.dto.response.ApiResponse;
import com.instagram.adapter.in.web.dto.response.UploadUrlResponse;
import com.instagram.domain.port.in.GenerateUploadUrlUseCase;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {
    private final GenerateUploadUrlUseCase generateUploadUrlUseCase;

    @PostMapping("/upload-url")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Generate a pre-signed PUT URL to upload a file directly to MinIO")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pre-signed URL returned"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid content type"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<ApiResponse<UploadUrlResponse>> getUploadUrl(
            @RequestBody @Valid UploadUrlRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        GenerateUploadUrlUseCase.UploadUrl result = generateUploadUrlUseCase.generateUploadUrl(
                new GenerateUploadUrlUseCase.Command(userId, req.filename(), req.contentType()));
        return ResponseEntity.ok(ApiResponse.ok(UploadUrlResponse.from(result)));
    }
}
