package ru.ztrixdev.projects.zellrapp.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.ztrixdev.projects.zellrapp.network.api.requests.ListingToFavoritesRequest
import ru.ztrixdev.projects.zellrapp.network.api.requests.NearbyListingsRequest
import ru.ztrixdev.projects.zellrapp.network.api.requests.UserIdRequest
import ru.ztrixdev.projects.zellrapp.network.api.results.AverageRatingAndReviewCountResult
import ru.ztrixdev.projects.zellrapp.network.api.results.NearbyListingsResult

interface IRFApi {

    @POST("rest/v1/rpc/get_nearby_listings")
    suspend fun getNearbyListings(
        @Body body: NearbyListingsRequest
    ): List<NearbyListingsResult>

    @POST("rest/v1/rpc/listing_to_favorites")
    suspend fun listingToFavorites(
        @Body body: ListingToFavoritesRequest
    )

    @POST("rest/v1/rpc/get_reviewee_avg_and_count")
    suspend fun getRevieweeAvgAndCount(
        @Body body: UserIdRequest
    ): List<AverageRatingAndReviewCountResult>

}