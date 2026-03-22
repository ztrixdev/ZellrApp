package ru.ztrixdev.projects.zellrapp.network.buckets

import io.github.jan.supabase.storage.storage
import ru.ztrixdev.projects.zellrapp.network.Supabase
import ru.ztrixdev.projects.zellrapp.network.buckets.BucketHelper.generatePath

object PfpBucket {
    private const val BUCKET_NAME = "Pfps"
    const val BUCKET_URL_PREFIX = "/storage/v1/object/public/Pfps/"

    suspend fun uploadPfp(displayName: String, fileExtension: String, file: ByteArray): String {
        val path = generatePath(displayName, fileExtension)
        Supabase.storage.from(BUCKET_NAME).upload(path, file)
        return path
    }
}