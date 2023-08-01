package com.example.soilsmetals.ui.theme

import androidx.annotation.DimenRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.soilsmetals.R

val defaultRoundness = 25.dp
val bigDefaultRoundness = 40.dp
val nanoRoundness = 4.dp

val Shape = Shapes(
    // default
    extraSmall = RoundedCornerShape(defaultRoundness),
    // big default
    small = RoundedCornerShape(bigDefaultRoundness),
    // addition styles
    medium = RoundedCornerShape(bottomStart = defaultRoundness, bottomEnd = defaultRoundness),
    large = RoundedCornerShape(topStart = defaultRoundness, topEnd = defaultRoundness),
    // points
    extraLarge = RoundedCornerShape(nanoRoundness)
)