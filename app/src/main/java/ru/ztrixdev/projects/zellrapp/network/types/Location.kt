package ru.ztrixdev.projects.zellrapp.network.types

import kotlinx.serialization.Serializable
import ru.ztrixdev.projects.zellrapp.network.serializers.LocationAsStringSerializer

@Serializable(with = LocationAsStringSerializer::class)
data class Location(
    val latitude: Double,
    val longitude: Double
)
