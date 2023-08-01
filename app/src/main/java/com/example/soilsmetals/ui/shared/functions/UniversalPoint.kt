package com.example.soilsmetals.ui.shared.functions

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.soilsmetals.R

@Composable
fun UniversalPoint(
    density: Density,
    offset: Offset,
    action: (() -> Unit)?,
    rotation: Float,
    size: Float
) {
    OutlinedButton(
        enabled = action != null,
        onClick = action ?: {},
        modifier = Modifier
            .graphicsLayer(
                translationX = density.run { (offset.x.dp - dimensionResource(R.dimen.compensation_offset_x)).toPx() },
                translationY = density.run { (offset.y.dp - dimensionResource(R.dimen.compensation_offset_y)).toPx() },
                rotationZ = -rotation,
                scaleX = 0.5f / size,
                scaleY = 0.5f / size
            ),
        border = BorderStroke(
            width = dimensionResource(R.dimen.simple_small_padding) / 1.2f,
            MaterialTheme.colorScheme.primary
        )
    ) {}
}