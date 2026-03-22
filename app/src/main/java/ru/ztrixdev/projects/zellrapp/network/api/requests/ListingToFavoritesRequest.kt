package ru.ztrixdev.projects.zellrapp.network.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingToFavoritesRequest (
    @SerialName("user_id_p")
    val userId: String,
    @SerialName("listing_id")
    val listingId: Int,
    val add: Boolean
)
