package ru.ivanov.API.models.Api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        String name,
        String job,
        String id,
        String createdAt
) {}