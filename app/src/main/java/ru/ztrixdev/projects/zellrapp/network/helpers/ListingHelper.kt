package ru.ztrixdev.projects.zellrapp.network.helpers

import ru.ztrixdev.projects.zellrapp.network.BACKEND_SERVICE_HOSTNAME
import ru.ztrixdev.projects.zellrapp.network.buckets.BucketHelper
import ru.ztrixdev.projects.zellrapp.network.buckets.ListingPicturesBucket
import ru.ztrixdev.projects.zellrapp.network.data.ListingInsert

object ListingHelper {
    enum class ListingProblems {
        NameTooShort, NameTooLong, InvalidPrice, InvalidPictureMap, InvalidGeoloc, DescriptionTooShort, DescriptionTooLong
    }


    val PRICE_BORDERS = Pair(0, 10_000_000)

    val NAME_LENGTH_BORDERS = Pair(6, 100)
    val DESCRIPTION_LENGTH_BORDERS = Pair(30, 2000)

    val LATITUDE_BORDERS = Pair(-90F, 90F)
    val LONGITUDE_BORDERS = Pair(-180F, 180F)

    fun ListingInsert.validate(): List<ListingProblems> {
        val problems = mutableListOf<ListingProblems>()

        if (this.price !in PRICE_BORDERS.first..PRICE_BORDERS.second)
            problems.add(ListingProblems.InvalidPrice)

        if (this.name.length < NAME_LENGTH_BORDERS.first)
            problems.add(ListingProblems.NameTooShort)
        if (this.name.length > NAME_LENGTH_BORDERS.second)
            problems.add(ListingProblems.NameTooLong)

        if (this.description.length < DESCRIPTION_LENGTH_BORDERS.first)
            problems.add(ListingProblems.DescriptionTooShort)
        if (this.description.length > DESCRIPTION_LENGTH_BORDERS.second)
            problems.add(ListingProblems.DescriptionTooLong)

        if (this.location.latitude !in LATITUDE_BORDERS.first..LATITUDE_BORDERS.second
            || this.location.longitude !in LONGITUDE_BORDERS.first..LONGITUDE_BORDERS.second)
            problems.add(ListingProblems.InvalidGeoloc)

        (listingPictures)
        if (this.listingPictures.any {
            pair ->
                pair.key < 0
                        || !pair.value.startsWith(BACKEND_SERVICE_HOSTNAME + ListingPicturesBucket.BUCKET_URL_PREFIX)
                        || pair.value.substringAfterLast('.') !in BucketHelper.allowedExtensions
            }
        ) {
            problems.add(ListingProblems.InvalidPictureMap)
        }

        return problems.toList()
    }

}