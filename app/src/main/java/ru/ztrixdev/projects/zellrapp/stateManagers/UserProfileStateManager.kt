package ru.ztrixdev.projects.zellrapp.stateManagers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.ztrixdev.projects.zellrapp.network.api.results.AverageRatingAndReviewCountResult
import ru.ztrixdev.projects.zellrapp.network.dao.ListingDao
import ru.ztrixdev.projects.zellrapp.network.dao.ReviewDao
import ru.ztrixdev.projects.zellrapp.network.dao.UserProfileDao
import ru.ztrixdev.projects.zellrapp.network.data.Listing
import ru.ztrixdev.projects.zellrapp.network.data.UserProfile

class UserProfileStateManager : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    val listings: StateFlow<List<Listing>> = _listings

    private val _rating = MutableStateFlow<AverageRatingAndReviewCountResult?>(null)
    val rating: StateFlow<AverageRatingAndReviewCountResult?> = _rating

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMyProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val profile = UserProfileDao.getMyProfile()
                if (profile != null) {
                    _userProfile.value = profile
                } else {
                    _error.value = "Profile not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProfileById(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val profile = UserProfileDao.getProfileById(userId)
                if (profile != null) {
                    _userProfile.value = profile
                } else {
                    _error.value = "Profile not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadListings() {
        viewModelScope.launch {
            _isLoading.value = true
            if (_userProfile.value == null)
                return@launch

            try {
                val listings = ListingDao.getListingsByUser(_userProfile.value!!.userId)
                _listings.value = listings
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRating() {
        viewModelScope.launch {
            _isLoading.value = true
            if (_userProfile.value == null)
                return@launch

            try {
                val data = ReviewDao.getRevieweesAverageRatingAndReviewCount(_userProfile.value!!.userId)
                _rating.value = data
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}