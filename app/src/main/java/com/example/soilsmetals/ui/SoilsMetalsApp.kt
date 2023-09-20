package com.example.soilsmetals.ui

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.screens.AddDecoyScreen
import com.example.soilsmetals.ui.screens.AddInformationScreen
import com.example.soilsmetals.ui.screens.AddPlaceholdersScreen
import com.example.soilsmetals.ui.screens.CollectionOfMapsScreen
import com.example.soilsmetals.ui.screens.CurrentMapScreen
import com.example.soilsmetals.ui.screens.SettingsScreen

@Composable
fun SoilsMetalsApp(
    askOrChangeTheme: (Boolean) -> Boolean,
    viewModel: SoilsMetalsViewModel = viewModel(factory = SoilsMetalsViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                )
                .clickable(onClick = { viewModel.tapTitle(navController) })
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(
                        if (uiState.titleIsFunctional) dimensionResource(R.dimen.top_bar__extended)
                        else dimensionResource(R.dimen.top_bar)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.title),
                    style = if ((AppCompatDelegate.getApplicationLocales()[0]?.language
                            ?: Locale.current.language) == "en"
                    )
                        MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.simple_padding),
                            bottom = dimensionResource(R.dimen.simple_small_padding)
                        )
                )
                if (uiState.titleIsFunctional) {
                    Row {
                        if (uiState.buttonBackwardIncluded) {
                            Button(
                                onClick = {
                                    viewModel.buttonBackwardAction(navController) { string ->
                                        navController.popBackStack(
                                            string,
                                            false
                                        )
                                    }
                                }
                            ) {
                                Text(
                                    when (navController.currentDestination!!.route!!) {
                                        SoilsMetalsViewModel.Screens.CurrentMap.name -> stringResource(
                                            R.string.exit_edit
                                        )

                                        SoilsMetalsViewModel.Screens.AddInformation.name -> stringResource(
                                            R.string.cancel
                                        )

                                        SoilsMetalsViewModel.Screens.AddDecoy.name -> stringResource(
                                            R.string.cancel
                                        )

                                        SoilsMetalsViewModel.Screens.AddPlaceholders.name -> stringResource(
                                            R.string.cancel
                                        )

                                        SoilsMetalsViewModel.Screens.CollectionOfMaps.name -> stringResource(
                                            R.string.exit
                                        )

                                        SoilsMetalsViewModel.Screens.Settings.name -> stringResource(
                                            R.string.empty
                                        )

                                        else -> {
                                            ""
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                        if (uiState.buttonForwardIncluded) {
                            Button(
                                onClick = {
                                    viewModel.buttonForwardAction(navController) { string ->
                                        navController.navigate(string)
                                    }
                                }
                            ) {
                                Text(
                                    when (navController.currentDestination!!.route!!) {
                                        SoilsMetalsViewModel.Screens.CurrentMap.name -> stringResource(
                                            R.string.add_placeholder
                                        )

                                        SoilsMetalsViewModel.Screens.AddInformation.name -> stringResource(
                                            R.string.resume
                                        )

                                        SoilsMetalsViewModel.Screens.AddDecoy.name -> stringResource(
                                            R.string.resume
                                        )

                                        SoilsMetalsViewModel.Screens.AddPlaceholders.name -> stringResource(
                                            R.string.resume
                                        )

                                        SoilsMetalsViewModel.Screens.CollectionOfMaps.name -> stringResource(
                                            R.string.create_map
                                        )

                                        SoilsMetalsViewModel.Screens.Settings.name -> stringResource(
                                            R.string.check
                                        )

                                        else -> {
                                            ""
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(
            Modifier
                .height(
                    dimensionResource(R.dimen.bottom_bar) +
                            dimensionResource(R.dimen.simple_padding) * 2
                )
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large
                )
                .align(Alignment.BottomCenter)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.simple_padding))
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.bottom_bar))
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    viewModel.updateTitleIsFunctional()
                    viewModel.updateEditMode(false)
                    viewModel.updateRequestedDocument(true)
                    viewModel.updateCurrentBitmapFile()
                    navController.popBackStack(
                        SoilsMetalsViewModel.Screens.CollectionOfMaps.name,
                        false
                    )
                }
            ) {
                Icon(
                    Icons.Rounded.Menu, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(dimensionResource(R.dimen.icons))
                )
            }
            IconButton(
                onClick = {
                    viewModel.updateTitleIsFunctional()
                    navController.navigate(SoilsMetalsViewModel.Screens.Settings.name)
                }
            ) {
                Icon(
                    Icons.Rounded.Build, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(dimensionResource(R.dimen.icons))
                )
            }
            if (navController.currentDestination?.route == SoilsMetalsViewModel.Screens.CurrentMap.name) {
                IconButton(
                    onClick = {
                        viewModel.updatePlaceholdersVisibility()
                    }
                ) {
                    Icon(
                        if (uiState.placeholdersVisible.not()) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(dimensionResource(R.dimen.icons))
                    )
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = SoilsMetalsViewModel.Screens.CollectionOfMaps.name,
            modifier = Modifier.zIndex(-1f)
        ) {

            composable(SoilsMetalsViewModel.Screens.CurrentMap.name) {
                CurrentMapScreen(viewModel, uiState, navController)
            }
            composable(SoilsMetalsViewModel.Screens.CollectionOfMaps.name) {
                CollectionOfMapsScreen(viewModel, uiState, navController)
            }
            composable(SoilsMetalsViewModel.Screens.Settings.name) {
                SettingsScreen(uiState, viewModel, askOrChangeTheme, navController)
            }
            composable(SoilsMetalsViewModel.Screens.AddDecoy.name) {
                AddDecoyScreen(uiState, viewModel, navController)
            }
            composable(SoilsMetalsViewModel.Screens.AddInformation.name) {
                AddInformationScreen(viewModel, uiState, navController)
            }
            composable(SoilsMetalsViewModel.Screens.AddPlaceholders.name) {
                AddPlaceholdersScreen(viewModel, uiState, navController)
            }
        }
        BackHandler {
            viewModel.updateTitleIsFunctional()
            viewModel.updateEditMode(false)
            viewModel.updateRequestedDocument(true)
            viewModel.updateCurrentBitmapFile()
            navController.popBackStack(
                SoilsMetalsViewModel.Screens.CollectionOfMaps.name,
                false
            )
        }
    }
}