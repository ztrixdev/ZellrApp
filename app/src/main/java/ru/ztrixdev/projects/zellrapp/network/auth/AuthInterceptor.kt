package ru.ztrixdev.projects.zellrapp.network.auth

import okhttp3.Interceptor
import okhttp3.Response
import ru.ztrixdev.projects.zellrapp.network.SUPABASE_API_KEY

class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("apikey", SUPABASE_API_KEY)

        tokenProvider()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}
