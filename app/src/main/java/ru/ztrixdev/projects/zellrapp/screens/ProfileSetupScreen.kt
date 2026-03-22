package ru.ztrixdev.projects.zellrapp.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.ztrixdev.projects.zellrapp.R
import ru.ztrixdev.projects.zellrapp.network.BACKEND_SERVICE_HOSTNAME
import ru.ztrixdev.projects.zellrapp.network.buckets.PfpBucket
import ru.ztrixdev.projects.zellrapp.network.helpers.UserProfileHelper
import ru.ztrixdev.projects.zellrapp.stateManagers.ProfileSetupStateManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    viewModel: ProfileSetupStateManager = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var displayName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var pfpUrl by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    
    val failedToUploadImageStr = stringResource(R.string.failed_to_upload_image)
    val profileValidationFailedStr = stringResource(R.string.profile_validation_failed)
    val profileCreationSuccessStr = stringResource(R.string.profile_creation_success)
    val profileCreationFailureStr = stringResource(R.string.profile_creation_failure)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                scope.launch {
                    isUploading = true
                    try {
                        val inputStream = context.contentResolver.openInputStream(it)
                        val bytes = inputStream?.readBytes()
                        if (bytes != null) {
                            val extension = context.contentResolver.getType(it)?.split("/")?.lastOrNull() ?: "jpg"
                            val path = PfpBucket.uploadPfp(displayName.ifBlank { "user" }, extension, bytes)
                            pfpUrl = BACKEND_SERVICE_HOSTNAME + PfpBucket.BUCKET_URL_PREFIX + path
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "${failedToUploadImageStr}: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isUploading = false
                    }
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.setup_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    scope.launch {
                        isSubmitting = true
                        try {
                            viewModel.finalize(displayName, pfpUrl, bio)
                            val result = viewModel.pushNewProfile()
                            if (result == viewModel.NOT_READY_TO_PUSH_STR) {
                                Toast.makeText(context, profileValidationFailedStr, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, profileCreationSuccessStr, Toast.LENGTH_SHORT).show()
                                onComplete()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message ?: profileCreationFailureStr, Toast.LENGTH_LONG).show()
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = !isSubmitting && !isUploading && displayName.isNotBlank() && pfpUrl.isNotBlank(),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.complete_setup), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // PFP Selection Circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (pfpUrl.isNotEmpty()) {
                    AsyncImage(
                        model = pfpUrl,
                        contentDescription = stringResource(R.string.profile_picture),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.upload_photo),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (isUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Display Name TextField
            OutlinedTextField(
                value = displayName,
                onValueChange = { if (it.length <= 50) displayName = it },
                label = { Text(stringResource(R.string.display_name)) },
                placeholder = { Text(stringResource(R.string.display_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = displayName.isNotEmpty() && !UserProfileHelper.isDisplayNameValid(displayName),
                supportingText = {
                    if (displayName.isNotEmpty() && !UserProfileHelper.isDisplayNameValid(displayName)) {
                        Text(stringResource(R.string.display_name_error))
                    }
                },
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bio TextField
            OutlinedTextField(
                value = bio,
                onValueChange = { if (it.length <= 160) bio = it },
                label = { Text(stringResource(R.string.bio_label)) },
                placeholder = { Text(stringResource(R.string.bio_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                minLines = 3,
                maxLines = 5,
                isError = bio.isNotEmpty() && !UserProfileHelper.isBioValid(bio),
                supportingText = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        if (bio.isNotEmpty() && !UserProfileHelper.isBioValid(bio)) {
                            Text(stringResource(R.string.bio_error))
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Text("${bio.length}/160")
                    }
                },
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
