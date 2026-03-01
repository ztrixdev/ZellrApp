package ru.ztrixdev.projects.zellrapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import ru.dgis.sdk.Context
import ru.ztrixdev.projects.zellrapp.network.dao.ListingDao
import ru.ztrixdev.projects.zellrapp.network.data.Listing

@Composable
fun MainScreen(dgisctx: Context) {
    val listings = remember { mutableStateListOf<Listing>()}
    LaunchedEffect(Unit) {
        listings.clear()
        listings.addAll(ListingDao.getListings())
    }
    if (listings.isNotEmpty()) {
        ListingOverviewScreen(listings[0], dgisctx)
    }

}

