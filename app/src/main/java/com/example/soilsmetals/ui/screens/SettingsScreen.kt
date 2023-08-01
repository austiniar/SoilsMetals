package com.example.soilsmetals.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalDialog
import com.example.soilsmetals.ui.shared.functions.UniversalInput

@Composable
fun SettingsScreen(
    uiState: SoilsMetalsUiState,
    viewModel: SoilsMetalsViewModel,
    askOrChangeTheme: (Boolean) -> Boolean,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.simple_padding))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            Modifier
                .height(
                    if (uiState.titleIsFunctional) dimensionResource(R.dimen.top_bar__extended)
                    else dimensionResource(R.dimen.top_bar)
                )
        )
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(dimensionResource(R.dimen.simple_padding))
                .width(dimensionResource(R.dimen.card_width))
        ) {
            Text(
                text = stringResource(R.string.account),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
            if (uiState.authOperation) {
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
                if (viewModel.currentUser() != null) {
                    Text(
                        text = stringResource(R.string.you_are_signed_in),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    Text(
                        text = if (viewModel.emailIsVerified(viewModel.currentUser()!!.email!!))
                            stringResource(R.string.you_are_able)
                        else stringResource(R.string.you_are_disabled),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = {
                            viewModel.startSignOut()
                        },
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.simple_padding))
                    ) {
                        Text(text = stringResource(R.string.sign_out))
                    }
                } else {
                    Text(
                        text = stringResource(R.string.you_are_not_signed_in),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    Text(
                        text = stringResource(R.string.signin_or_signup),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    UniversalInput(
                        label = stringResource(R.string.login),
                        value = uiState.email,
                        action = { viewModel.updateEmail(it) },
                        keyboardType = KeyboardType.Email,
                        isSingleLine = true,
                        isError = uiState.emailError != null,
                        size = dimensionResource(R.dimen.card_width),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.emailError != null) {
                        Text(
                            text = stringResource(uiState.emailError),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    UniversalInput(
                        label = stringResource(R.string.password),
                        value = uiState.password,
                        action = { viewModel.updatePassword(it) },
                        keyboardType = KeyboardType.Password,
                        isSingleLine = true,
                        isError = uiState.passwordError != null,
                        size = dimensionResource(R.dimen.card_width),
                        placeholder = "",
                        opaque = true
                    )
                    if (uiState.passwordError != null) {
                        Text(
                            text = stringResource(uiState.passwordError),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.updateTitleIsFunctional()
                            viewModel.startRequestSignIn(uiState.email, uiState.password)
                        },
                        modifier = Modifier
                            .padding(top = dimensionResource(R.dimen.simple_padding))
                    ) {
                        Text(text = stringResource(R.string.resume))
                    }
                }
            }
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))

        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(dimensionResource(R.dimen.simple_padding))
                .width(dimensionResource(R.dimen.card_width))
        ) {
            val position by animateDpAsState(
                if ((AppCompatDelegate.getApplicationLocales()[0]?.language
                        ?: Locale.current.language) == "en"
                ) 0.dp else 60.dp,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                ), label = ""
            )

            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
            Row {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_picture_size))
                        .width(dimensionResource(R.dimen.simple_small_padding))
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable(
                            onClick = {
                                viewModel.updateTitleIsFunctional()
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat
                                        .forLanguageTags(
                                            if ((AppCompatDelegate.getApplicationLocales()[0]?.language
                                                    ?: Locale.current.language) == "en"
                                            )
                                                "ru" else "en"
                                        )
                                )
                            }
                        )
                        .offset(y = position)
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.simple_small_padding))
                            .height(dimensionResource(R.dimen.card_picture_size) / 2)
                            .background(
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                Column(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_picture_size)),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.language_en),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    Text(
                        text = stringResource(R.string.language_ru),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))

        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(dimensionResource(R.dimen.simple_padding))
                .width(dimensionResource(R.dimen.card_width))
        ) {
            val position by animateDpAsState(
                if (askOrChangeTheme(true)) 60.dp else 0.dp,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                ), label = ""
            )

            Text(
                text = stringResource(R.string.theme),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
            Row {
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_picture_size))
                        .width(dimensionResource(R.dimen.simple_small_padding))
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable(
                            onClick = { askOrChangeTheme(false) }
                        )
                        .offset(y = position)
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.simple_small_padding))
                            .height(dimensionResource(R.dimen.card_picture_size) / 2)
                            .background(
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.simple_padding)))
                Column(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.card_picture_size)),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.theme_light),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
                    Text(
                        text = stringResource(R.string.theme_dark),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.simple_padding)))
        }

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