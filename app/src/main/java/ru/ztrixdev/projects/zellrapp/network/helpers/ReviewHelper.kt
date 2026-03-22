package ru.ztrixdev.projects.zellrapp.network.helpers

import ru.ztrixdev.projects.zellrapp.network.dao.ListingDao
import ru.ztrixdev.projects.zellrapp.network.dao.UserProfileDao
import ru.ztrixdev.projects.zellrapp.network.data.insert.ReviewInsert

object ReviewHelper {

    enum class ReviewProblems  {
        InvalidComment, RelatedListingDoesntExist, InvalidReviewerId, InvalidRevieweeId, CantLeaveReviewsAboutYourself, RelatedListingsOwnerIsntTheReviewee
    }

    suspend fun isReviewReady(review: ReviewInsert): MutableSet<ReviewProblems> {
        val problems = mutableSetOf<ReviewProblems>()

        if (!isReviewCommentValid(review.comment)) {
            problems.add(ReviewProblems.InvalidComment)
        }

        val listing = ListingDao.getListingById(review.relatedListingId)
        val reviewer = UserProfileDao.getProfileById(review.reviewerId)
        val reviewee = UserProfileDao.getProfileById(review.revieweeId)

        if (listing == null)
            problems.add(ReviewProblems.RelatedListingDoesntExist)
        if (reviewee == null)
            problems.add(ReviewProblems.InvalidRevieweeId)
        if (reviewer == null)
            problems.add(ReviewProblems.InvalidReviewerId)

        if (reviewer != null && reviewee != null) {
            if (reviewer.userId == reviewee.userId) {
                problems.add(ReviewProblems.CantLeaveReviewsAboutYourself)
            }
        }

        if (listing != null && reviewer != null && reviewee != null) {
            if (listing.userId != reviewee.userId) {
                problems.add(ReviewProblems.RelatedListingsOwnerIsntTheReviewee)
            }

            if (reviewer.userId == listing.userId) {
                problems.add(ReviewProblems.CantLeaveReviewsAboutYourself)
            }
        }

        return problems
    }

    fun isReviewCommentValid(comment: String) = comment.length in 0..500

}
