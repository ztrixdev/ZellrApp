package ru.ztrixdev.projects.zellrapp.network.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserIdRequest(
    @SerialName("user_id_p")
    val userId: String
)
