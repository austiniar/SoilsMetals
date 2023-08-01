package com.example.soilsmetals.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalDialog
import com.example.soilsmetals.ui.shared.functions.UniversalInput

@Composable
fun AddDecoyScreen(
    uiState: SoilsMetalsUiState,
    viewModel: SoilsMetalsViewModel,
    navController: NavHostController
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { viewModel.updateNewMapUri(it) }
    )

    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.simple_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
            .height(
                (if (uiState.titleIsFunctional) dimensionResource(R.dimen.top_bar__extended) else
                    dimensionResource(R.dimen.top_bar)) + dimensionResource(R.dimen.simple_padding)
            )
        )
        Text(
            text = stringResource(R.string.create_header),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        UniversalInput(
            label = stringResource(R.string.name),
            value = uiState.mapName,
            action = { viewModel.updateNewMapName(it) },
            keyboardType = KeyboardType.Text,
            isSingleLine = true,
            isError = uiState.mapNameError != null,
            size = dimensionResource(R.dimen.large_input),
            placeholder = "",
            opaque = false
        )
        if (uiState.mapNameError != null) {
            Text(
                stringResource(uiState.mapNameError),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.width(dimensionResource(R.dimen.large_input)),
                textAlign = TextAlign.Left
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        ) {
            Text(stringResource(R.string.select_picture))
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        AsyncImage(
            model = ImageRequest.Builder(viewModel.mapsRepository.providedContext())
                .data(uiState.newMapUri ?: Uri.parse(viewModel.context().getString(R.string.default_uri)))
                .crossfade(true)
                .build(),
            contentDescription = null,
            error = painterResource(R.drawable.corrupted),
            placeholder = painterResource(R.drawable.loading),
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.bottom_bar) + dimensionResource(R.dimen.simple_padding) * 2))
    }

    if (uiState.showDialog) {
        Spacer(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.transparent_gray))
        )
        UniversalDialog(
            viewModel = viewModel,
            navController = navController
        )
    }
}