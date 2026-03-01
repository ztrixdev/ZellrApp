package ru.ztrixdev.projects.zellrapp.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.ztrixdev.projects.zellrapp.network.types.Location

@Serializable
data class Listing (
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,

    val name: String,
    val description: String,

    @SerialName("listing_pictures")
    val listingPictures: Map<Int, String>,
    val price: Int,
    @SerialName("user_id")
    val userId: String,

    @SerialName("location_wkt")
    val location: Location
)