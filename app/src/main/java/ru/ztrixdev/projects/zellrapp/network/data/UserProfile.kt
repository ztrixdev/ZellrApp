package ru.ztrixdev.projects.zellrapp.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserProfile (
    @SerialName("created_at")
    val createdAt: String,

    @SerialName("display_name")
    val displayName: String,
    val pfp: String,
    val bio: String,

    val favorites: List<Int>,
    @SerialName("user_id")
    val userId: String,
)


