package ru.ztrixdev.projects.zellrapp.network.helpers

import ru.ztrixdev.projects.zellrapp.network.BACKEND_SERVICE_HOSTNAME
import ru.ztrixdev.projects.zellrapp.network.buckets.PfpBucket

object UserProfileHelper {
    fun isDisplayNameValid(name: String): Boolean {
        return (name.length in 3..50
                && name.matches(Regex("^[a-zA-Z0-9 ]+$")))
    }

    fun isBioValid(bio: String): Boolean {
        return (bio.length in 0..160)
    }

    fun isPfpValid(url: String): Boolean {
        return url.startsWith(BACKEND_SERVICE_HOSTNAME + PfpBucket.BUCKET_URL_PREFIX)
    }

}