package ru.ztrixdev.projects.zellrapp.network.auth

sealed interface AuthResponse {
    data class Success(val message: String): AuthResponse
    data class Error(val message: String): AuthResponse
}
