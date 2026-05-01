package com.example.employeemanager.model

data class LoginDto(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val username: String
)
