package ru.ztrixdev.projects.zellrapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.ztrixdev.projects.zellrapp.network.api.IRFApi
import ru.ztrixdev.projects.zellrapp.network.auth.AuthInterceptor
import ru.ztrixdev.projects.zellrapp.network.auth.SessionManager

object RetrofitClient {

    private const val BASE_URL = BACKEND_SERVICE_HOSTNAME

    private val okHttpWithAuth = OkHttpClient.Builder()
        .addInterceptor(
            AuthInterceptor {
                SessionManager.getAccessToken()
            }
        )
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val api: IRFApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpWithAuth)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(IRFApi::class.java)
    }
}