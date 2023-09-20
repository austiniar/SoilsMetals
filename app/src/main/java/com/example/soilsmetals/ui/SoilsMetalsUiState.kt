package com.example.soilsmetals.ui

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import com.google.firebase.firestore.QuerySnapshot
import java.io.File

data class SoilsMetalsUiState(
    val titleIsFunctional: Boolean = false,
    val buttonBackwardIncluded: Boolean = false,
    val buttonForwardIncluded: Boolean = false,

    // collection of maps
    val requestedDocumentsCurrentMap: QuerySnapshot? = null,
    val requestedDocumentsCollectionOfMaps: QuerySnapshot? = null,
    val requestedDocumentsVerification: QuerySnapshot? = null,
    val loading: Boolean = false,
    val authOperation: Boolean = false,
    val corruptedMaps: List<String> = listOf(),
    val loadedMaps: List<String> = listOf(),

    // addInformationScreen
    val streetName: String = "",
    val streetNameHasError: Int? = null,
    val values: List<String> = listOf("", "", "", "", ""),
    val errors: List<Boolean> = listOf(false, false, false, false, false),
    val errorMessages: List<Int?> = listOf(null, null, null, null, null),
    val additionInformation: String = "",
    val pointsAsListOfOffsets: List<Offset> = listOf(),

    // current map screen
    val currentBitmapFile: File? = null,
    val currentBitmapFileWidth: Int = 1920,
    val currentBitmapFileHeight: Int = 1080,
    val currentMapId: String = "",
    val editMode: Boolean = false,
    val readMode: Boolean = false,
    val showDialog: Boolean = false,
    val placeholdersVisible: Boolean = true,

    // add decoy screen
    val mapName: String = "",
    val mapNameError: Int? = null,
    val newMapUri: Uri? = null,

    // register screen
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val email: String = "",
    val password: String = ""
)
