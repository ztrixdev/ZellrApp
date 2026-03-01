package ru.ztrixdev.projects.zellrapp.network.dao

import android.util.Log
import io.github.jan.supabase.postgrest.from
import retrofit2.HttpException
import ru.ztrixdev.projects.zellrapp.network.RetrofitClient
import ru.ztrixdev.projects.zellrapp.network.Supabase
import ru.ztrixdev.projects.zellrapp.network.api.requests.NearbyListingsRequest
import ru.ztrixdev.projects.zellrapp.network.api.results.NearbyListingsResult
import ru.ztrixdev.projects.zellrapp.network.data.Listing
import ru.ztrixdev.projects.zellrapp.network.data.ListingInsert

object ListingDao {
    private const val TABLE_NAME = "listings_with_loc_wkt"

    suspend fun getListings(): List<Listing> {
        return Supabase.from(TABLE_NAME)
            .select().decodeList<Listing>()
    }

    suspend fun getNearbyListings(req: NearbyListingsRequest): List<NearbyListingsResult> {
        try {
            val result = RetrofitClient.api.getNearbyListings(req)
            return result
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SupabaseError", errorBody ?: "No body")
        }
        return emptyList()
    }

    suspend fun insertListing(listing: ListingInsert): String {
        return Supabase.from(TABLE_NAME).insert(listing).data
    }
}