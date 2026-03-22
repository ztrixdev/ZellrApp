package ru.ztrixdev.projects.zellrapp.network.api.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AverageRatingAndReviewCountResult(
    @SerialName("average_rating")
    val averageRating: Double?,
    @SerialName("review_count")
    val reviewCount: Int?
)
