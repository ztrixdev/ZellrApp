package ru.ztrixdev.projects.zellrapp.network.data.insert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileInsert (
    @SerialName("display_name")
    val displayName: String,
    val pfp: String,
    val bio: String,
    val favorites: List<Int>
)
