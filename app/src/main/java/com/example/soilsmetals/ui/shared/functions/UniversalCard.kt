package com.example.soilsmetals.ui.shared.functions

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import java.io.File

@Composable
fun UniversalCard(
    uri: File,
    name: String,
    actionOpen: () -> Unit,
    actionEdit: () -> Unit,
    actionDelete: () -> Unit,
    viewModel: SoilsMetalsViewModel
) {
    var isExtended by rememberSaveable { mutableStateOf(false) }
    val translation by animateDpAsState(
        if (isExtended) 380.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Row(
        modifier = Modifier
            .height(dimensionResource(R.dimen.card_height))
    ) {
        Spacer(
            Modifier
                .width((LocalConfiguration.current.screenWidthDp.dp - dimensionResource(R.dimen.card_width)) / 2)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
                .zIndex(1f)
        )
        Column(
            modifier = Modifier
                .height(dimensionResource(R.dimen.card_height))
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .width(dimensionResource(R.dimen.card_width))
                .wrapContentWidth(unbounded = true)
                .padding(top = dimensionResource(R.dimen.simple_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .height(
                        dimensionResource(R.dimen.card_height) - dimensionResource(R.dimen.simple_padding) * 2 - dimensionResource(
                            R.dimen.simple_small_padding
                        )
                    )
                    .offset(x = (dimensionResource(R.dimen.card_width) / 2 + dimensionResource(R.dimen.simple_padding) / 2 - translation))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(viewModel.mapsRepository.providedContext())
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.loading),
                    error = painterResource(R.drawable.corrupted),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.card_picture_size))
                        .clip(MaterialTheme.shapes.extraSmall)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .width(
                            dimensionResource(R.dimen.card_width) -
                                    dimensionResource(R.dimen.simple_padding) * 2 -
                                    dimensionResource(R.dimen.card_picture_size)
                        )
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = actionOpen) {
                        Text(
                            stringResource(R.string.open)
                        )
                    }
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                Column(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.card_width)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = actionEdit,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(stringResource(R.string.edit))
                    }
                    Button(
                        onClick = actionDelete,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
            Box(
                Modifier
                    .width(dimensionResource(R.dimen.card_width) - dimensionResource(R.dimen.simple_padding) * 2)
                    .height(dimensionResource(R.dimen.simple_small_padding))
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.extraSmall
                    )
            ) {
                Box(
                    modifier = Modifier.offset(
                        x = translation * 0.58947366f
                    )
                ) {
                    Spacer(
                        Modifier
                            .width(dimensionResource(R.dimen.card_picture_size))
                            .fillMaxHeight()
                            .background(
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable(onClick = { isExtended = !isExtended })
                )
            }
        }
        Spacer(
            Modifier
                .width((LocalConfiguration.current.screenWidthDp.dp - dimensionResource(R.dimen.card_width)) / 2)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        )
    }
    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
}