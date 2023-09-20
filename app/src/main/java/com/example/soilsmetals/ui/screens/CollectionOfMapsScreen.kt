package com.example.soilsmetals.ui.screens

import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.soilsmetals.R
import com.example.soilsmetals.ui.SoilsMetalsUiState
import com.example.soilsmetals.ui.SoilsMetalsViewModel
import com.example.soilsmetals.ui.shared.functions.UniversalCard
import com.example.soilsmetals.ui.shared.functions.UniversalDialog
import java.io.File

@Composable
fun CollectionOfMapsScreen(
    viewModel: SoilsMetalsViewModel,
    uiState: SoilsMetalsUiState,
    navController: NavController
) {
    if (uiState.requestedDocumentsCollectionOfMaps == null) {
        viewModel.startRequestAllDocumentsAtDirectory("Maps", 0)
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
        LazyColumn {
            item {
                Spacer(
                    Modifier.height(
                        (if (uiState.titleIsFunctional) dimensionResource(R.dimen.top_bar__extended)
                        else dimensionResource(R.dimen.top_bar)) + dimensionResource(R.dimen.simple_padding)
                    )
                )
            }

            for (elem in uiState.requestedDocumentsCollectionOfMaps) {
                viewModel.startRequestToReplaceThisFileData(elem.id)
                item {
                    UniversalCard(
                        uri = File(
                            Environment.getExternalStorageDirectory().absolutePath + "/Download",
                            "${elem.id}.png"
                        ),
                        name = elem.id,
                        actionOpen = {
                            viewModel.updateTitleIsFunctional()
                            viewModel.updateRequestedDocument(false)
                            viewModel.cardButtonOpenAction(elem.id) { string ->
                                navController.navigate(string)
                            }
                        },
                        actionEdit = {
                            if (viewModel.currentUser() == null) {
                                viewModel.updateShowDialog()
                            }
                            else if (viewModel.emailIsVerified(viewModel.currentUser()!!.email!!)) {
                                viewModel.updateTitleIsFunctional()
                                viewModel.updateRequestedDocument(false)
                                viewModel.updateEditMode(true)
                                viewModel.cardButtonOpenAction(elem.id) { string ->
                                    navController.navigate(string)
                                }
                            } else {
                                viewModel.updateShowDialog()
                            }
                        },
                        actionDelete = {
                            if (viewModel.currentUser() == null) {
                                viewModel.updateShowDialog()
                            }
                            else if (viewModel.emailIsVerified(viewModel.currentUser()!!.email!!)) {
                                viewModel.updateTitleIsFunctional()
                                viewModel.startRequestToDeleteDocumentAtDirectory("Maps", elem.id, true)
                            } else {
                                viewModel.updateShowDialog()
                            }
                        },
                        viewModel = viewModel,
                        elem.id in uiState.loadedMaps
                    )
                }
            }

            item {
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