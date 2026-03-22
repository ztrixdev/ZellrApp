package ru.ztrixdev.projects.zellrapp.network.data.insert

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewInsert (
    @SerialName("related_listing_id")
    val relatedListingId: Int,

    @SerialName("reviewer_id")
    val reviewerId: String,
    @SerialName("reviewee_id")
    val revieweeId: String,

    val rating: Int,
    val comment: String
)