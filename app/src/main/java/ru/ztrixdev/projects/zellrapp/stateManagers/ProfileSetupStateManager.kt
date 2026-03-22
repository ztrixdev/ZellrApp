package ru.ztrixdev.projects.zellrapp.stateManagers

import androidx.lifecycle.ViewModel
import ru.ztrixdev.projects.zellrapp.network.dao.UserProfileDao
import ru.ztrixdev.projects.zellrapp.network.data.insert.UserProfileInsert
import ru.ztrixdev.projects.zellrapp.network.helpers.UserProfileHelper

class ProfileSetupStateManager : ViewModel() {
    private var _pfpUrl: String = ""
    private var _displayName: String = ""
    private var _bio: String = ""
    private var _readyToPush: Boolean = false

    val FinalizationFailedException = Exception("Finalization of user's profile properties failed, as they don't satisfy Zellr's requirements. Check profile's validity and try again.")

    fun finalize(newName: String, newPfpUrl: String, newBio: String) {
        _readyToPush = false

        if (UserProfileHelper.isPfpValid(newPfpUrl))
            _pfpUrl = newPfpUrl
        if (UserProfileHelper.isDisplayNameValid(newName))
            _displayName = newName
        if (UserProfileHelper.isBioValid(newBio))
            _bio = newBio

        if (
            _pfpUrl.isBlank()
            || _displayName.isBlank()
            || _bio.isBlank()
        )
            throw FinalizationFailedException
        else
            _readyToPush = true
    }


    val NOT_READY_TO_PUSH_STR: String = "Not ready to push"
    suspend fun pushNewProfile(): String {
        if (!_readyToPush)
            return NOT_READY_TO_PUSH_STR

        val newProfile = UserProfileInsert(
            displayName = _displayName,
            bio = _bio,
            pfp = _pfpUrl,
            favorites = emptyList()
        )

        return UserProfileDao.createProfile(newProfile)
    }
}

