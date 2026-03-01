package ru.ztrixdev.projects.zellrapp.network.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NearbyListingsRequest(
    val lon: Double,
    val lat: Double,
    @SerialName("radiusmeters")
    val radiusmeters: Int
)
