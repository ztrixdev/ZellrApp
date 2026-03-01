package ru.ztrixdev.projects.zellrapp.network.auth

import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.flow
import ru.ztrixdev.projects.zellrapp.network.Supabase

object SessionManager {
    fun getSessionStatus() = flow {
        Supabase.auth.sessionStatus.collect {
            emit(it)
        }
    }

    fun getAccessToken(): String? {
        return Supabase.auth.currentAccessTokenOrNull()
    }
}