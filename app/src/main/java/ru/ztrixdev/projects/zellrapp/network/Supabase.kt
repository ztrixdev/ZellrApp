package ru.ztrixdev.projects.zellrapp.network

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

val Supabase = createSupabaseClient(
    supabaseUrl = BACKEND_SERVICE_HOSTNAME,
    supabaseKey = SUPABASE_API_KEY
) {
    install(Postgrest)
    install(Storage)
    install(Auth)
}

