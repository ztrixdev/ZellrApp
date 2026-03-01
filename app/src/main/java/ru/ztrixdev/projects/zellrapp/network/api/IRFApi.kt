package ru.ztrixdev.projects.zellrapp.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.ztrixdev.projects.zellrapp.network.api.requests.NearbyListingsRequest
import ru.ztrixdev.projects.zellrapp.network.api.results.NearbyListingsResult

interface IRFApi {

    @POST("rest/v1/rpc/get_nearby_listings")
    suspend fun getNearbyListings(
        @Body body: NearbyListingsRequest
    ): List<NearbyListingsResult>

}