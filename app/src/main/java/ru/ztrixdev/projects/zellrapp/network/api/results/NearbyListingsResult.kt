package ru.ztrixdev.projects.zellrapp.network.api.results

import kotlinx.serialization.Serializable
import ru.ztrixdev.projects.zellrapp.network.serializers.LocationAsStringSerializer
import ru.ztrixdev.projects.zellrapp.network.types.Location

@Serializable
data class NearbyListingsResult(
    val id: Int,
    val name: String,
    @Serializable(with = LocationAsStringSerializer::class)
    val location: Location,
    val distance: Double
)
