package ru.ztrixdev.projects.zellrapp.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.ztrixdev.projects.zellrapp.network.types.Location

@Serializable
data class ListingInsert (
    val name: String,
    val description: String,

    @SerialName("listing_pictures")
    val listingPictures: Map<Int, String>,
    val price: Int,

    @SerialName("location_wkt")
    val location: Location
)