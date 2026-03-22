package ru.ztrixdev.projects.zellrapp.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review (
    val id: Int,


    @SerialName("created_at")
    val createdAt: String,

    @SerialName("related_listing_id")
    val relatedListingId: Int,

    @SerialName("reviewer_id")
    val reviewerId: String,
    @SerialName("reviewee_id")
    val revieweeId: String,

    val rating: Int,
    val comment: String
)
