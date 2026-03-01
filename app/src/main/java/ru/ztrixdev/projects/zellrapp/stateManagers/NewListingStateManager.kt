package ru.ztrixdev.projects.zellrapp.stateManagers

import androidx.lifecycle.ViewModel
import ru.ztrixdev.projects.zellrapp.network.dao.ListingDao
import ru.ztrixdev.projects.zellrapp.network.helpers.ListingHelper.validate
import ru.ztrixdev.projects.zellrapp.network.data.ListingInsert

class NewListingStateManager: ViewModel() {
    suspend fun pushNewListing(listing: ListingInsert): Pair<Boolean, String> {

        if (listing.validate().isEmpty()) {
            val res = ListingDao.insertListing(listing)
            return Pair(true, res)
        } else {
            (listing.validate())
            return Pair(false, "Listing contains problems and cannot be pushed to the database.")
        }
    }
}
