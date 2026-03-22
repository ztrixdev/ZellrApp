package ru.ztrixdev.projects.zellrapp.screens

import android.text.format.DateUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import ru.ztrixdev.projects.zellrapp.R
import ru.ztrixdev.projects.zellrapp.network.api.results.AverageRatingAndReviewCountResult
import ru.ztrixdev.projects.zellrapp.network.data.Listing
import ru.ztrixdev.projects.zellrapp.network.serializers.SUPABASE_DATE_FORMAT
import ru.ztrixdev.projects.zellrapp.stateManagers.UserProfileStateManager
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userId: String? = null,
    onBack: () -> Unit = {},
    viewModel: UserProfileStateManager = koinViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val rating by viewModel.rating.collectAsState()
    val listings by viewModel.listings.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadProfileById(userId)
        } else {
            viewModel.loadMyProfile()
        }
    }
    LaunchedEffect(userProfile) {
        if (userProfile!=null) {
            viewModel.loadRating()
            viewModel.loadListings()
        }
    }

    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.error, error ?: ""), color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        if (userId != null) viewModel.loadProfileById(userId) else viewModel.loadMyProfile()
                    }) {
                        Text(stringResource(R.string.retry))
                    }
                }
            } else if (userProfile != null && rating != null) {
                val profile = userProfile!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // pfp
                        AsyncImage(
                            model = profile.pfp,
                            contentDescription = stringResource(R.string.profile_picture),
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        NameJoinDateScore(profile.displayName, profile.createdAt, rating!!)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    BioBlock(profile.bio)
                    Listings(listings)
                }
            } else {
                Text(
                    text = stringResource(R.string.profile_not_found),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun NameJoinDateScore(displayName: String, createdAt: String, rating: AverageRatingAndReviewCountResult) {
    Column {
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        val dateText = remember(createdAt) {
            try {
                val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val date = SUPABASE_DATE_FORMAT.parse(createdAt)
                if (date != null) outputFormat.format(date) else createdAt
            } catch (e: Exception) {
                createdAt
            }
        }

        if (rating.averageRating != null && rating.reviewCount != null) {
            RatingBar(rating.averageRating)

            Text(
                text = stringResource(R.string.review_count, rating.reviewCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }

        Text(
            text = stringResource(R.string.joined_on, dateText),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun Listings(listings: List<Listing>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(items = listings) { it ->
            ListingCard(it)
        }
    }
}

@Composable
fun ListingCard(
    listing: Listing
) {
    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        ),
        shape = RectangleShape,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(260.dp)
    ) {
        Column(
        ) {
            AsyncImage(
                model = listing.listingPictures[0],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.05f))
            )

            Spacer(modifier = Modifier.height(4.dp))
            val dateText = remember(listing.createdAt) {
                try {
                    val date = SUPABASE_DATE_FORMAT.parse(listing.createdAt)
                    if (date != null) {
                        DateUtils.getRelativeTimeSpanString(
                            date.time,
                            System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS
                        ).toString()
                    } else {
                        listing.createdAt
                    }
                } catch (e: Exception) {
                    listing.createdAt
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = listing.name,
                    style =
                            MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = listing.price.toString() + " " + stringResource(R.string.currency),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    textAlign = TextAlign.End
                )

            }

        }
    }
}

@Composable
private fun RatingBar(rating: Double, max: Int = 5) {
    Row {
        for (i in 1..max) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5 -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }

            Icon(
                imageVector = icon,
                contentDescription = "rating stars",
                tint = Color(0xFFFFC107) // gold coloring
            )
        }
    }
}

@Composable
private fun BioBlock(bio: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Text(
            text = stringResource(R.string.bio_label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        val isLongBio = bio.length > 200

        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )

        if (isLongBio) {
            Text(
                text = if (expanded) stringResource(R.string.show_less) else stringResource(R.string.read_more),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}


