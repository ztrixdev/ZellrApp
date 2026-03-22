package ru.ztrixdev.projects.zellrapp.network.dao

import android.util.Log
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import retrofit2.HttpException
import ru.ztrixdev.projects.zellrapp.network.RetrofitClient
import ru.ztrixdev.projects.zellrapp.network.Supabase
import ru.ztrixdev.projects.zellrapp.network.api.requests.UserIdRequest
import ru.ztrixdev.projects.zellrapp.network.api.results.AverageRatingAndReviewCountResult
import ru.ztrixdev.projects.zellrapp.network.data.Review
import ru.ztrixdev.projects.zellrapp.network.data.insert.ReviewInsert
import ru.ztrixdev.projects.zellrapp.network.helpers.ReviewHelper

object ReviewDao {
    private val TABLE_NAME = "reviews"

    private val REVIEWEE_COLUMN_NAME = "reviewee_id"
    private val REVIEWER_COLUMN_NAME = "reviewer_id"

    suspend fun getReviewsByReviewee(uuid: String): List<Review> {
        return Supabase.from(TABLE_NAME)
            .select(Columns.ALL)
            { filter {
                    eq(REVIEWEE_COLUMN_NAME, uuid)
                }
            }.decodeList<Review>()
    }

    suspend fun getReviewsByReviewer(uuid: String): List<Review> {
        return Supabase.from(TABLE_NAME)
            .select(Columns.ALL)
            { filter {
                    eq(REVIEWER_COLUMN_NAME, uuid)
                }
            }.decodeList<Review>()
    }

    suspend fun pushReview(review: ReviewInsert): String {
        val reviewProblems = ReviewHelper.isReviewReady(review)
        if (!reviewProblems.isEmpty())
            return "The review contains the following problems and cannot be pushed: " +
                    "$reviewProblems"
        return Supabase.from(TABLE_NAME)
            .insert(review)
            .data
    }

    suspend fun getRevieweesAverageRatingAndReviewCount(revieweeId: String): AverageRatingAndReviewCountResult? {
        try {
            val result = RetrofitClient.api.getRevieweeAvgAndCount(UserIdRequest(revieweeId))
            return result.first()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SupabaseError", errorBody ?: "No body")
        }
        return null
    }
}
