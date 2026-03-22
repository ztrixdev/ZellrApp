package ru.ztrixdev.projects.zellrapp.screens

import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.dgis.sdk.Context
import ru.dgis.sdk.compose.map.MapComposable
import ru.dgis.sdk.compose.map.MapComposableState
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.geometry.GeoPointWithElevation
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.GraphicsPreset
import ru.dgis.sdk.map.MapObjectManager
import ru.dgis.sdk.map.MapOptions
import ru.dgis.sdk.map.MapRenderMode
import ru.dgis.sdk.map.Marker
import ru.dgis.sdk.map.MarkerOptions
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.map.imageFromResource
import ru.ztrixdev.projects.zellrapp.R
import ru.ztrixdev.projects.zellrapp.network.data.Listing
@Composable
fun ListingOverviewScreen(listing: Listing, mapContext: Context) {


}

@Composable
private fun Description(description: String) {
    Text(
        text = stringResource(R.string.listing_description),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )
}

@Composable
private fun LocatedAt(latitude: Double, longitude: Double, mapContext: Context) {
    Text(
        text = stringResource(R.string.located_at),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(200.dp)
            .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.5f),
                shape = RectangleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        val mapState = remember {
            MapComposableState(
                MapOptions().apply {
                    position = CameraPosition(GeoPoint(latitude, longitude), zoom = Zoom(16f))
                    graphicsPreset = GraphicsPreset.IMMERSIVE
                    renderMode = MapRenderMode.SURFACE
                }
            )
        }

        val objectManagerRef = remember { mutableStateOf<MapObjectManager?>(null) }

        MapComposable(
            state = mapState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        LaunchedEffect(mapState.map.collectAsState().value) {
            mapState.map.value?.let { map ->
                val objManager = MapObjectManager(map)
                objectManagerRef.value = objManager

                val icon = imageFromResource(mapContext, R.drawable.location, size = Size(64,64))

                val marker = Marker(
                    MarkerOptions(
                        position = GeoPointWithElevation(latitude, longitude),
                        icon = icon,
                    )
                )
                objManager.addObject(marker)
            }
        }
    }
}

@Composable
private fun NameAndPrice(name: String, price: Int) {
    Row(
       modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            softWrap = true,
            overflow = TextOverflow.Clip,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$ ${price}",
            style = MaterialTheme.typography.titleLarge ,
            fontWeight = FontWeight.ExtraBold,
            softWrap = true,
            overflow = TextOverflow.Clip,
            modifier = Modifier.weight(0.4f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Gallery(images: Map<Int, String>) {
    val pagerState = rememberPagerState { images.size }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            pageSpacing = 32.dp,
            contentPadding = PaddingValues(horizontal = 64.dp)
        ) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .dropShadow(
                        shape = RoundedCornerShape(16.dp),
                        shadow = Shadow(
                            radius = 12.dp,
                            spread = 4.dp,
                            color = Color.Black.copy(alpha = 0.45f),
                        )
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1c1a1b))
                    .sizeIn(maxWidth = 280.dp, maxHeight = 280.dp)
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            repeat(images.size) { index ->
                val color =
                    if (pagerState.currentPage == index) Color.White else Color.LightGray.copy(alpha = 0.6f)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}


