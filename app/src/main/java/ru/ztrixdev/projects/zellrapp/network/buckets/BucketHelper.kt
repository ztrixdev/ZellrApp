package ru.ztrixdev.projects.zellrapp.network.buckets

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object BucketHelper {
    val allowedExtensions = listOf("jpeg", "png", "webp", "jpg")

    @OptIn(ExperimentalUuidApi::class)
    fun generatePath(associatedName: String, fileExtension: String): String {
        if (!allowedExtensions.contains(fileExtension))
            throw IllegalArgumentException("Extension not allowed")

        val sb = StringBuilder()
        sb.append(Uuid.random())
        sb.append('_')
        sb.append(URLEncoder.encode(associatedName, StandardCharsets.UTF_8.toString()))
        sb.append('_')
        sb.append(System.currentTimeMillis())
        sb.append('.')
        sb.append(fileExtension)
        return sb.toString()
    }
}