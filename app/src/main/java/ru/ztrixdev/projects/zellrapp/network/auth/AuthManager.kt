package ru.ztrixdev.projects.zellrapp.network.auth

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import ru.ztrixdev.projects.zellrapp.network.Supabase

object AuthManager {
    fun signUpWithEmail(displayName: String, email: String, password: String): Flow<AuthResponse> = flow {
        try {
            val result = Supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = Json.encodeToJsonElement(mapOf("display_name" to displayName)) as JsonObject?
            }
            emit(AuthResponse.Success("Sign up successful"))
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.message ?: "Unknown error"))
        }
    }

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> = flow {
        try {
            val result = Supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            emit(AuthResponse.Success("Login successful"))
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.message ?: "Unknown error"))
        }
    }
}
