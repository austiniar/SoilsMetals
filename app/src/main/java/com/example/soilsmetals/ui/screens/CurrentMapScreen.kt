package com.example.soilsmetals.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalDialog
import com.example.soilsmetals.ui.shared.functions.UniversalPoint
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CurrentMapScreen(
    viewModel: SoilsMetalsViewModel,
    uiState: SoilsMetalsUiState,
    navController: NavHostController
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }
    var angle by remember { mutableStateOf(0f) }

    if (uiState.requestedDocumentsCurrentMap == null) {
        viewModel.startRequestAllDocumentsAtDirectory("Maps/${uiState.currentMapId}/Places", 1)
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.loading),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.card_picture_size))
                    .clip(CircleShape)
                    .align(Alignment.Center),
            )
        }
    } else {
        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, zoom, rotation ->
                            val prevScale = scale
                            val newScale = scale * zoom
                            offset = (offset + centroid / prevScale).rotateBy(rotation) -
                                    (centroid / newScale + pan / prevScale)
                            scale = newScale
                            angle += rotation
                        }
                    )
                }
                .fillMaxSize()
        ) {
            val density = LocalDensity.current

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = -offset.x * scale
                        translationY = -offset.y * scale
                        scaleX = scale
                        scaleY = scale
                        rotationZ = angle
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .wrapContentSize(unbounded = true)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (navController.currentDestination!!.route!! == SoilsMetalsViewModel.Screens.AddPlaceholders.name)
                                viewModel.updatePoints(density.run {
                                    Offset(
                                        it.x.toDp().value,
                                        it.y.toDp().value
                                    )
                                })
                        }
                    }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(viewModel.context())
                        .data(uiState.currentBitmapFile)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.loading),
                    error = painterResource(R.drawable.corrupted),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(uiState.currentBitmapFileWidth.dp * 0.03f))
                        .width(uiState.currentBitmapFileWidth.dp)
                        .height(uiState.currentBitmapFileHeight.dp)
                )

                if (navController.currentDestination!!.route!! == SoilsMetalsViewModel.Screens.AddPlaceholders.name) {
                    for (elem in uiState.pointsAsListOfOffsets) {
                        UniversalPoint(
                            density = density,
                            offset = elem,
                            action = null,
                            rotation = angle,
                            size = scale
                        )
                    }
                } else {
                    for (elem in uiState.requestedDocumentsCurrentMap) {
                        for (pointOffset in viewModel.parseDoubles(elem.get("points") as ArrayList<Double>)) {
                            UniversalPoint(
                                density = density,
                                offset = pointOffset,
                                action = {
                                    viewModel.updateTitleIsFunctional()
                                    viewModel.updateUserPopulatingValuesWith(elem)
                                    if (uiState.editMode) {
                                        viewModel.buttonForwardAction(navController) { string ->
                                            navController.navigate(string)
                                        }
                                    } else {
                                        viewModel.updateReadMode()
                                    }
                                },
                                rotation = angle,
                                size = scale
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.readMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.transparent_gray))
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                Modifier
                    .height(LocalConfiguration.current.screenHeightDp.dp / 1.9f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(dimensionResource(R.dimen.simple_padding))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = uiState.streetName,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = { viewModel.updateReadMode() }) {
                        Icon(
                            Icons.Rounded.Close, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(dimensionResource(R.dimen.icons) * 4)
                        )
                    }
                }
                Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                Text(
                    text = stringResource(R.string.zc),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (viewModel.calculateZc() > 32) colorResource(R.color.transparent_red_variant)
                            else colorResource(R.color.soft_green),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(dimensionResource(R.dimen.simple_padding))
                ) {
                    Text(
                        text = viewModel.calculateZc().toString(),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.simple_padding))
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.card_picture_size)),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.pollution_power),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.simple_padding)),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (viewModel.calculateZc() < 16) stringResource(R.string.pollution_power__normal)
                            else if (viewModel.calculateZc() in 16.0..32.0) stringResource(R.string.pollution_power__semidanger)
                            else if (viewModel.calculateZc() in 32.0..128.0) stringResource(R.string.pollution_power__danger)
                            else stringResource(R.string.pollution_power__height_danger),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.simple_padding)),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                for (i in 0..uiState.values.size - viewModel.positionsCount) {
                    if (i % viewModel.positionsCount != 0) {
                        continue
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    Column {
                        Text(
                            text = uiState.values[i],
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = dimensionResource(R.dimen.simple_padding)),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0x35726489),
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(dimensionResource(R.dimen.simple_padding))
                        ) {
                            Text(
                                text = uiState.values[i + 1],
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = dimensionResource(R.dimen.simple_padding))
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .fillMaxWidth()
                                    .height(dimensionResource(R.dimen.card_picture_size) * 1.5f),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = uiState.values[i + 2] + " " + stringResource(R.string.kg_mg),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(
                                            top = dimensionResource(R.dimen.simple_padding),
                                            start = dimensionResource(R.dimen.simple_padding)
                                        ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = stringResource(R.string.more_than_limit) + " " + uiState.values[i + 3] + " " + stringResource(
                                        R.string.times
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(
                                        start = dimensionResource(R.dimen.simple_padding)
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = stringResource(R.string.pollution_power) + " " + uiState.values[i + 4],
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(
                                            bottom = dimensionResource(R.dimen.simple_padding),
                                            start = dimensionResource(R.dimen.simple_padding)
                                        ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding) * 2))
                Text(
                    text = stringResource(R.string.addition_information),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                Text(
                    text = uiState.additionInformation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.bottom_bar) + dimensionResource(R.dimen.simple_padding) * 2))
            }
        }
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

fun Offset.rotateBy(angle: Float): Offset {
    val inRadians = angle * PI / 180
    return Offset(
        (x * cos(inRadians) - y * sin(inRadians)).toFloat(),
        (x * sin(inRadians) + y * cos(inRadians)).toFloat(),
    )
}