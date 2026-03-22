package ru.ztrixdev.projects.zellrapp.network.dao

import android.util.Log
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import retrofit2.HttpException
import ru.ztrixdev.projects.zellrapp.network.RetrofitClient
import ru.ztrixdev.projects.zellrapp.network.Supabase
import ru.ztrixdev.projects.zellrapp.network.api.requests.ListingToFavoritesRequest
import ru.ztrixdev.projects.zellrapp.network.data.UserProfile
import ru.ztrixdev.projects.zellrapp.network.data.insert.UserProfileInsert
import ru.ztrixdev.projects.zellrapp.network.helpers.UserProfileHelper

object UserProfileDao {
    private const val TABLE_NAME = "user_profiles"

    private const val USER_ID_COLUMN = "user_id"
    private const val DISPLAY_NAME_COLUMN = "display_name"
    private const val BIO_COLUMN = "bio"
    private const val PFP_COLUMN = "pfp"
    private const val FAVORITES_COLUMN = "favorites"


    suspend fun getProfileById(uuid: String): UserProfile? {
        return Supabase.from(TABLE_NAME)
            .select(Columns.ALL)
            { filter {
                    eq(USER_ID_COLUMN, uuid)
                }
            }.decodeSingleOrNull<UserProfile>()
    }

    suspend fun getMyProfile(): UserProfile? {
        val uid = Supabase.auth.currentUserOrNull()?.id ?: return null
        val profile = Supabase.from(TABLE_NAME)
            .select(Columns.ALL)
            { filter {
                    eq(USER_ID_COLUMN, uid)
                }
            }
            .decodeSingleOrNull<UserProfile>()
        return profile
    }

    suspend fun createProfile(up: UserProfileInsert): String {
        return Supabase.from(TABLE_NAME).insert(up).data
    }

    suspend fun updatePfp(url: String) {
        val profileExists = getMyProfile() != null
        val pfpValid = UserProfileHelper.isPfpValid(url)

        if (profileExists) {
            Supabase.from(TABLE_NAME).update(
                mapOf(PFP_COLUMN to url),
            ) { filter {
                    eq(USER_ID_COLUMN, Supabase.auth.currentUserOrNull()!!.id)
                }
            }
        }
    }

    suspend fun updateBio(bio: String) {
        val profileExists = getMyProfile() != null
        val bioValid = UserProfileHelper.isBioValid(bio)

        if (profileExists && bioValid) {
            Supabase.from(TABLE_NAME).update(
                mapOf(BIO_COLUMN to bio),
            ) { filter {
                    eq(USER_ID_COLUMN, Supabase.auth.currentUserOrNull()!!.id)
                }
            }
        }
    }

    suspend fun updateDisplayName(name: String) {
        val profileExists = getMyProfile() != null
        val displayNameValid = UserProfileHelper.isDisplayNameValid(name)


        if (profileExists && displayNameValid) {
            Supabase.from(TABLE_NAME).update(
                mapOf(DISPLAY_NAME_COLUMN to name),
            ) { filter {
                    eq(USER_ID_COLUMN, Supabase.auth.currentUserOrNull()!!.id)
                }
            }
        }
    }

    suspend fun addToFavorites(listingId: Int)  {
        val profileExists = getMyProfile() != null
        val listingExists = ListingDao.getListingById(listingId) != null

        if (profileExists && listingExists) {
            try {
                RetrofitClient.api.listingToFavorites(
                    ListingToFavoritesRequest(userId = Supabase.auth.currentUserOrNull()!!.id, listingId = listingId, add = true)
                )
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SupabaseError", errorBody ?: "No body")
            }
        }
    }


}