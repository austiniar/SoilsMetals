package com.example.soilsmetals.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalDialog
import com.example.soilsmetals.ui.shared.functions.UniversalInput

@Composable
fun AddInformationScreen(
    viewModel: SoilsMetalsViewModel,
    uiState: SoilsMetalsUiState,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            Modifier
                .height(
                    (if (uiState.titleIsFunctional) dimensionResource(R.dimen.top_bar__extended)
                    else dimensionResource(R.dimen.top_bar)) + dimensionResource(R.dimen.simple_padding) * 2
                )
        )
        Text(
            text = stringResource(R.string.add_information),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        UniversalInput(
            label = stringResource(R.string.street_name),
            value = uiState.streetName,
            action = { viewModel.updateStreetName(it) },
            keyboardType = KeyboardType.Text,
            isSingleLine = true,
            isError = false,
            size = dimensionResource(R.dimen.large_input),
            placeholder = stringResource(R.string.input_street),
            opaque = false
        )
        if (uiState.streetNameHasError != null) {
            Text(
                text = stringResource(uiState.streetNameHasError),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        for (i in 1..(uiState.values.count() / viewModel.positionsCount)) {
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    UniversalInput(
                        label = stringResource(R.string.metall),
                        value = uiState.values[i.dec() * viewModel.positionsCount + 0],
                        action = { viewModel.updateValueInValues(i.dec() * viewModel.positionsCount + 0, it) },
                        keyboardType = KeyboardType.Text,
                        isSingleLine = true,
                        isError = uiState.errors[i.dec() * viewModel.positionsCount + 0],
                        size = dimensionResource(R.dimen.small_input),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.errors[i.dec() * viewModel.positionsCount + 0]) {
                        Text(
                            stringResource(uiState.errorMessages[i.dec() * viewModel.positionsCount + 0] ?: R.string.empty),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(dimensionResource(R.dimen.small_input)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_small_padding)))
                Column {
                    UniversalInput(
                        label = stringResource(R.string.sign),
                        value = uiState.values[i.dec() * viewModel.positionsCount + 1],
                        action = { viewModel.updateValueInValues(i.dec() * viewModel.positionsCount + 1, it) },
                        keyboardType = KeyboardType.Text,
                        isSingleLine = true,
                        isError = uiState.errors[i.dec() * viewModel.positionsCount + 1],
                        size = dimensionResource(R.dimen.small_input),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.errors[i.dec() * viewModel.positionsCount + 1]) {
                        Text(
                            stringResource(uiState.errorMessages[i.dec() * viewModel.positionsCount + 1] ?: R.string.empty),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(dimensionResource(R.dimen.small_input)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

            }
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                Spacer(
                    Modifier.width(
                        (LocalConfiguration.current.screenWidthDp.dp -
                                dimensionResource(R.dimen.large_input)) / 2
                    )
                )
                Column {
                    UniversalInput(
                        label = stringResource(R.string.containment),
                        value = uiState.values[i.dec() * viewModel.positionsCount + 2],
                        action = { viewModel.updateValueInValues(i.dec() * viewModel.positionsCount + 2, it) },
                        keyboardType = KeyboardType.Decimal,
                        isSingleLine = true,
                        isError = uiState.errors[i.dec() * viewModel.positionsCount + 2],
                        size = dimensionResource(R.dimen.large_input),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.errors[i.dec() * viewModel.positionsCount + 2]) {
                        Text(
                            stringResource(uiState.errorMessages[i.dec() * viewModel.positionsCount + 2] ?: R.string.empty),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(dimensionResource(R.dimen.small_input)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                Column {
                    UniversalInput(
                        label = stringResource(R.string.limit),
                        value = uiState.values[i.dec() * viewModel.positionsCount + 3],
                        action = { viewModel.updateValueInValues(i.dec() * viewModel.positionsCount + 3, it) },
                        keyboardType = KeyboardType.Decimal,
                        isSingleLine = true,
                        isError = uiState.errors[i.dec() * viewModel.positionsCount + 3],
                        size = dimensionResource(R.dimen.large_input),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.errors[i.dec() * viewModel.positionsCount + 3]) {
                        Text(
                            stringResource(uiState.errorMessages[i.dec() * viewModel.positionsCount + 3] ?: R.string.empty),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(dimensionResource(R.dimen.small_input)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                Column {
                    UniversalInput(
                        label = stringResource(R.string.pollution_power),
                        value = uiState.values[i.dec() * viewModel.positionsCount + 4],
                        action = { viewModel.updateValueInValues(i.dec() * viewModel.positionsCount + 4, it) },
                        keyboardType = KeyboardType.Text,
                        isSingleLine = true,
                        isError = uiState.errors[i.dec() * viewModel.positionsCount + 4],
                        size = dimensionResource(R.dimen.large_input),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.errors[i.dec() * viewModel.positionsCount + 4]) {
                        Text(
                            stringResource(uiState.errorMessages[i.dec() * viewModel.positionsCount + 4] ?: R.string.empty),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(dimensionResource(R.dimen.small_input)),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(
                    Modifier.width(
                        (LocalConfiguration.current.screenWidthDp.dp -
                                dimensionResource(R.dimen.large_input)) / 2
                    )
                )
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding) * 2))
        UniversalInput(
            label = stringResource(R.string.addition_information),
            value = uiState.additionInformation,
            action = { viewModel.updateAdditionInformation(it) },
            keyboardType = KeyboardType.Text,
            isSingleLine = false,
            isError = false,
            size = LocalConfiguration.current.screenWidthDp.dp - dimensionResource(R.dimen.simple_padding) * 2,
            placeholder = "",
            opaque = false
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding) * 2 + dimensionResource(R.dimen.bottom_bar)))
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