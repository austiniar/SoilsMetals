package com.example.soilsmetals.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalDialog

@Composable
fun AddPlaceholdersScreen(
    viewModel: SoilsMetalsViewModel,
    uiState: SoilsMetalsUiState,
    navController: NavHostController
) {
    CurrentMapScreen(viewModel, uiState, navController)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { viewModel.updatePointsRemove() },
            modifier = Modifier
                .padding(
                    bottom = dimensionResource(R.dimen.bottom_bar) + dimensionResource(R.dimen.simple_padding) * 3,
                    end = dimensionResource(R.dimen.simple_padding)
                )
                .size(dimensionResource(R.dimen.icons) * 3)
                .align(Alignment.BottomEnd)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraSmall
                )
        ) {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icons) * 1.25f),
                tint = MaterialTheme.colorScheme.onPrimary
            )
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

