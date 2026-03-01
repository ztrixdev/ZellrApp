package ru.ztrixdev.projects.zellrapp.network.buckets

import io.github.jan.supabase.storage.FileUploadResponse
import io.github.jan.supabase.storage.storage
import ru.ztrixdev.projects.zellrapp.network.Supabase
import ru.ztrixdev.projects.zellrapp.network.buckets.BucketHelper.generatePath

object ListingPicturesBucket {
    private const val BUCKET_NAME = "ListingPictures"
    const val BUCKET_URL_PREFIX = "/storage/v1/object/public/ListingPictures/"

    suspend fun uploadImage(listingName: String, fileExtension: String, file: ByteArray): FileUploadResponse {
        return Supabase.storage.from(BUCKET_NAME).upload(generatePath(listingName, fileExtension), file)
    }
}
