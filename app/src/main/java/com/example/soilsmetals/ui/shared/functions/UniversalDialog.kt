package com.example.soilsmetals.ui.shared.functions

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.soilsmetals.R
import com.example.soilsmetals.data.internet
import com.example.soilsmetals.ui.SoilsMetalsViewModel

@Composable
fun UniversalDialog(
    viewModel: SoilsMetalsViewModel,
    navController: NavController
) {
    if (!internet) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowDialog() },
            title = {
                Text(
                    stringResource(R.string.fun_unavailable),
                    textAlign = TextAlign.Center,
                    style = if ((AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.current.language) == "en")
                        MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    stringResource(R.string.have_to_connect),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                OutlinedButton(onClick = { viewModel.updateShowDialog() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            shape = MaterialTheme.shapes.extraSmall,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    } else if (viewModel.currentUser() != null && viewModel.emailIsVerified(viewModel.currentUser()!!.email!!)) {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowDialog() },
            title = {
                Text(
                    stringResource(R.string.fun_unavailable),
                    textAlign = TextAlign.Center,
                    style = if ((AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.current.language) == "en")
                        MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    stringResource(R.string.uncathed_exception),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                OutlinedButton(onClick = { viewModel.updateShowDialog() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            shape = MaterialTheme.shapes.extraSmall,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    } else {
        AlertDialog(
            onDismissRequest = { viewModel.updateShowDialog() },
            title = {
                Text(
                    stringResource(R.string.fun_unavailable),
                    textAlign = TextAlign.Center,
                    style = if ((AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.current.language) == "en")
                        MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    if (viewModel.currentUser() == null)
                        stringResource(R.string.signin_or_signup) else stringResource(R.string.wait),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                OutlinedButton(onClick = { viewModel.updateShowDialog() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                if (viewModel.currentUser() == null) {
                    OutlinedButton(
                        onClick = {
                            viewModel.updateTitleIsFunctional()
                            viewModel.updateShowDialog()
                            navController.navigate(SoilsMetalsViewModel.Screens.Settings.name)
                        }
                    ) {
                        Text(stringResource(R.string.to_signin))
                    }
                }
            },
            shape = MaterialTheme.shapes.extraSmall,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}