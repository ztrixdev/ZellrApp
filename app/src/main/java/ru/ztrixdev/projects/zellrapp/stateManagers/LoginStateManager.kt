package ru.ztrixdev.projects.zellrapp.stateManagers

import androidx.lifecycle.ViewModel
import ru.ztrixdev.projects.zellrapp.network.auth.AuthManager
import ru.ztrixdev.projects.zellrapp.network.auth.AuthResponse

class LoginStateManager() : ViewModel() {
    suspend fun onSignUpClick(displayName: String, email: String, password: String): Pair<Boolean, String> {
        return try {
            var result: Pair<Boolean, String>? = null
            AuthManager.signUpWithEmail(displayName, email, password)
                .collect { response ->
                    result = when (response) {
                        is AuthResponse.Success -> Pair(true, response.message)
                        is AuthResponse.Error -> Pair(false, response.message)
                    }
                }
            result ?: Pair(false, "No response")
        } catch (e: Exception) {
            Pair(false, e.localizedMessage ?: "Sign up failed")
        }
    }

    suspend fun onLoginClick(email: String, password: String): Pair<Boolean, String> {
        return try {
            var result: Pair<Boolean, String>? = null
            AuthManager.signInWithEmail(email, password)
                .collect { response ->
                    result = when (response) {
                        is AuthResponse.Success -> Pair(true, response.message)
                        is AuthResponse.Error -> Pair(false, response.message)
                    }
                }
            result ?: Pair(false, "No response")
        } catch (e: Exception) {
            Pair(false, e.localizedMessage ?: "Login failed")
        }
    }
}