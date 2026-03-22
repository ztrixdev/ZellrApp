package ru.ztrixdev.projects.zellrapp.network.serializers

import java.text.SimpleDateFormat
import java.util.TimeZone
import android.R.attr.timeZone
import java.util.Locale

val SUPABASE_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}


