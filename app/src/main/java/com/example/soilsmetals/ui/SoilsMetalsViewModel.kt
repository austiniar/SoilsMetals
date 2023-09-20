package com.example.soilsmetals.ui

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.soilsmetals.R
import com.example.soilsmetals.SoilsMetalsApplication
import com.example.soilsmetals.data.MapsRepository
import com.example.soilsmetals.data.internet
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class SoilsMetalsViewModel(val mapsRepository: MapsRepository) : ViewModel() {
    val uiState = MutableStateFlow(SoilsMetalsUiState())
    val positionsCount = 5

    enum class Screens {
        CurrentMap,
        CollectionOfMaps,
        Settings,
        AddDecoy,
        AddInformation,
        AddPlaceholders
    }

    init {
        startRequestAllDocumentsAtDirectory("Users", 2)
    }

    fun tapTitle(navController: NavHostController) {
        if (uiState.value.titleIsFunctional) {
            updateTitleIsFunctional()
        } else {
            when (navController.currentDestination!!.route!!.toString()) {
                Screens.CurrentMap.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = uiState.value.editMode,
                        buttonForwardIncluded = !uiState.value.editMode
                    )
                }

                Screens.AddInformation.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = true,
                        buttonForwardIncluded = true
                    )
                }

                Screens.AddDecoy.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = true,
                        buttonForwardIncluded = true
                    )
                }

                Screens.AddPlaceholders.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = true,
                        buttonForwardIncluded = true
                    )
                }

                Screens.CollectionOfMaps.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = true,
                        buttonForwardIncluded = true
                    )
                }

                Screens.Settings.name -> uiState.update {
                    it.copy(
                        titleIsFunctional = true,
                        buttonBackwardIncluded = false,
                        buttonForwardIncluded = true
                    )
                }
            }
        }
    }

    fun buttonForwardAction(navController: NavHostController, navAction: (String) -> Unit) {
        updateTitleIsFunctional()

        when (navController.currentDestination!!.route!!) {
            Screens.CurrentMap.name -> {
                if (currentUser() == null || !internet) {
                    updateShowDialog()
                    return
                }
                if (emailIsVerified(currentUser()!!.email!!) && internet) {
                    navAction(Screens.AddInformation.name)
                } else {
                    updateShowDialog()
                }
            }

            Screens.AddInformation.name -> {
                if (checkValues() && internet) {
                    navAction(Screens.AddPlaceholders.name)
                } else if (checkValues() && !internet) {
                    updateShowDialog()
                }
            }

            Screens.AddPlaceholders.name -> {
                if (internet) {
                    navAction(Screens.CurrentMap.name)
                    if (uiState.value.pointsAsListOfOffsets.isNotEmpty()) {
                        startRequestToCreateStreetAtDirectory("Maps/${uiState.value.currentMapId}/Places")
                    } else {
                        startRequestToDeleteDocumentAtDirectory(
                            "Maps/${uiState.value.currentMapId}/Places",
                            uiState.value.streetName,
                            false
                        )
                    }
                } else {
                    updateShowDialog()
                }
            }

            Screens.Settings.name -> {
                updateTitleIsFunctional()
                if (internet) {
                    startRequestAllDocumentsAtDirectory("Users", 2)
                } else {
                    updateShowDialog()
                }
            }

            Screens.AddDecoy.name -> {
                if (internet) {
                    if (checkNewMapName()) {
                        navAction(Screens.CollectionOfMaps.name)
                        startRequestToCreateDecoyAtDirectory("Maps")
                    }
                } else {
                    updateShowDialog()
                }
            }

            Screens.CollectionOfMaps.name -> {
                if (currentUser() == null || !internet) {
                    updateShowDialog()
                    return
                }
                if (emailIsVerified(currentUser()!!.email!!) && internet) {
                    updateNewMapName("")
                    navAction(Screens.AddDecoy.name)
                } else {
                    updateShowDialog()
                }
            }
        }
    }

    fun buttonBackwardAction(navController: NavHostController, navAction: (String) -> Unit) {
        updateTitleIsFunctional()

        when (navController.currentDestination!!.route!!) {
            Screens.AddInformation.name -> {
                navAction(Screens.CurrentMap.name)
            }

            Screens.CurrentMap.name -> {
                updateEditMode(false)
                updateTitleIsFunctional()
            }

            Screens.AddPlaceholders.name -> {
                navAction(Screens.CurrentMap.name)
            }

            Screens.CollectionOfMaps.name -> (context() as Activity).finish()

            Screens.AddDecoy.name -> {
                navAction(Screens.CollectionOfMaps.name)
                updateDecoyInformation()
            }

            Screens.Settings.name -> {}
        }
    }

    private fun parsePoints(): ArrayList<Double> {
        val array = arrayListOf<Double>()
        for (elem in uiState.value.pointsAsListOfOffsets) {
            array += arrayListOf(elem.x.toDouble(), elem.y.toDouble())
        }
        return array
    }

    fun parseDoubles(doubles: ArrayList<Double>): List<Offset> {
        val array = arrayListOf<Offset>()
        for (i in 0 until doubles.lastIndex) {
            if (i % 2 != 0) {
                continue
            }
            array += listOf(Offset(doubles[i].toFloat(), doubles[i + 1].toFloat()))
        }
        return array
    }

    fun updateStreetName(name: String) {
        if (!uiState.value.editMode) {
            uiState.update {
                it.copy(
                    streetName = name, streetNameHasError = null
                )
            }
        } else {
            uiState.update {
                it.copy(
                    streetNameHasError = R.string.you_are_in_editing
                )
            }
        }
    }

    private fun updateLoading() {
        uiState.update {
            it.copy(
                loading = !uiState.value.loading
            )
        }
    }

    private fun updateDecoyInformation() {
        uiState.update {
            it.copy(
                mapName = "",
                newMapUri = null,
                mapNameError = null
            )
        }
    }

    fun updateNewMapUri(uri: Uri?) {
        uiState.update {
            it.copy(
                newMapUri = uri
            )
        }
    }

    fun updateNewMapName(name: String) {
        uiState.update {
            it.copy(
                mapName = name
            )
        }
    }

    fun updateShowDialog() {
        uiState.update {
            it.copy(
                showDialog = !uiState.value.showDialog
            )
        }
    }

    fun updateTitleIsFunctional() {
        uiState.update {
            it.copy(
                titleIsFunctional = false
            )
        }
    }

    fun updateEmail(input: String) {
        uiState.update {
            it.copy(
                email = input
            )
        }
    }

    fun updatePassword(input: String) {
        uiState.update {
            it.copy(
                password = input
            )
        }
    }

    fun updateAdditionInformation(string: String) {
        uiState.update {
            it.copy(
                additionInformation = string
            )
        }
    }

    fun updatePoints(offset: Offset) {
        uiState.update {
            it.copy(
                pointsAsListOfOffsets = uiState.value.pointsAsListOfOffsets + listOf(offset)
            )
        }
    }

    fun updateReadMode() {
        uiState.update {
            it.copy(
                readMode = !uiState.value.readMode
            )
        }
    }

    fun updateEditMode(value: Boolean) {
        uiState.update {
            it.copy(
                editMode = value
            )
        }
    }

    fun updatePlaceholdersVisibility() {
        uiState.update {
            it.copy(placeholdersVisible = uiState.value.placeholdersVisible.not())
        }
    }

    fun updatePointsRemove() {
        uiState.update {
            it.copy(
                pointsAsListOfOffsets = if (uiState.value.pointsAsListOfOffsets.lastIndex > 0)
                    uiState.value.pointsAsListOfOffsets.slice(0 until uiState.value.pointsAsListOfOffsets.lastIndex)
                else listOf()
            )
        }
    }

    fun updateValueInValues(index: Int, value: String) {
        val secondCondition =
            index % positionsCount == 1 && value.length !in 1..2
        if (value.isBlank() || secondCondition) {
            val prevErrors = uiState.value.errors
            val newErrors = prevErrors.slice(0 until index) + listOf(true) +
                    if (index != prevErrors.lastIndex) prevErrors.slice((index + 1)..prevErrors.lastIndex)
                    else listOf()

            val prevErrorMessages = uiState.value.errorMessages
            val newErrorMessages =
                prevErrorMessages.slice(0 until index) + listOf(if (secondCondition) R.string.two_signs_needed else R.string.must_provide_value) +
                        if (index != prevErrorMessages.lastIndex) prevErrorMessages.slice((index + 1)..prevErrorMessages.lastIndex)
                        else listOf()

            uiState.update {
                it.copy(
                    errors = newErrors, errorMessages = newErrorMessages
                )
            }
        } else {
            val prevErrors = uiState.value.errors
            val newErrors = prevErrors.slice(0 until index) + listOf(false) +
                    if (index != prevErrors.lastIndex) prevErrors.slice((index + 1)..prevErrors.lastIndex)
                    else listOf()

            val prevErrorMessages = uiState.value.errorMessages
            val newErrorMessages =
                prevErrorMessages.slice(0 until index) + listOf(null) +
                        if (index != prevErrorMessages.lastIndex) prevErrorMessages.slice((index + 1)..prevErrorMessages.lastIndex)
                        else listOf()
            if (index == uiState.value.errors.lastIndex) {
                uiState.update {
                    it.copy(
                        values = uiState.value.values + listOf("", "", "", "", ""),
                        errors = uiState.value.errors + listOf(false, false, false, false, false),
                        errorMessages = uiState.value.errorMessages + listOf(
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                }
            } else {
                uiState.update {
                    it.copy(
                        errors = newErrors, errorMessages = newErrorMessages
                    )
                }
            }

        }
        val prevValues = uiState.value.values
        val newValues = prevValues.slice(0 until index) + listOf(value) +
                if (index != prevValues.lastIndex)
                    prevValues.slice((index + 1)..prevValues.lastIndex)
                else listOf()
        uiState.update {
            it.copy(
                values = newValues
            )
        }
    }

    private fun checkValues(): Boolean {
        var isLastCorrectCondition =
            uiState.value.values.slice((uiState.value.values.size - positionsCount)..uiState.value.values.lastIndex)
                .toSet().map { it.isBlank() }.count() == 1

        for ((index, element) in uiState.value.values.slice(0..(if (uiState.value.errors.size > positionsCount && isLastCorrectCondition) uiState.value.errors.lastIndex - positionsCount else uiState.value.errors.lastIndex))
            .withIndex()) {
            if (element.isBlank()) {
                updateValueInValues(index, "")
                isLastCorrectCondition = false
            }
        }

        if (uiState.value.streetName.isBlank()) {
            uiState.update {
                it.copy(
                    streetNameHasError = R.string.you_didnt_provide_street
                )
            }
            isLastCorrectCondition = false
        } else {
            uiState.update {
                it.copy(
                    streetNameHasError = null
                )
            }
        }

        return isLastCorrectCondition
    }

    private fun checkNewMapName(): Boolean {
        if (uiState.value.mapName.isBlank()) {
            uiState.update {
                it.copy(
                    mapNameError = R.string.must_provide_value
                )
            }
            return false
        }
        return true
    }

    fun clearCurrentMapValues() {
        uiState.update {
            it.copy(
                streetName = "",
                streetNameHasError = null,
                values = listOf("", "", "", "", ""),
                errors = listOf(false, false, false, false, false),
                errorMessages = listOf(null, null, null, null, null),
                additionInformation = "",
                pointsAsListOfOffsets = listOf()
            )
        }
    }

    fun calculateZc(): Float {
        var sumOfAverageElements = 0f
        for (i in 1..uiState.value.values.size / positionsCount) {
            sumOfAverageElements += uiState.value.values[i.dec() * positionsCount + 2].toFloat()
        }
        return sumOfAverageElements - uiState.value.values.size / positionsCount
    }

    fun updateRequestedDocument(mode: Boolean) {
        when (mode) {
            true -> {
                uiState.update {
                    it.copy(
                        requestedDocumentsCollectionOfMaps = null
                    )
                }
            }

            false -> {
                uiState.update {
                    it.copy(
                        requestedDocumentsCurrentMap = null
                    )
                }
            }
        }
    }

    fun updateCurrentBitmapFile() {
        uiState.update {
            it.copy(
                currentBitmapFile = null
            )
        }
    }

    private fun updateAuthOperationStatus(value: Boolean) {
        uiState.update {
            it.copy(
                authOperation = value
            )
        }
    }

    fun updateUserPopulatingValuesWith(document: DocumentSnapshot) {
        uiState.update {
            it.copy(
                streetName = document.id,
                values = document.get("values") as ArrayList<String>,
                additionInformation = document.get("additionalInfo") as String,
                pointsAsListOfOffsets = parseDoubles(document.get("points") as ArrayList<Double>)
            )
        }
        val size = uiState.value.values.lastIndex
        uiState.update {
            it.copy(
                errors = uiState.value.errors.slice(0..size),
                errorMessages = uiState.value.errorMessages.slice(0..size)
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SoilsMetalsApplication)
                val mapsRepository = application.container.mapsRepository
                SoilsMetalsViewModel(mapsRepository = mapsRepository)
            }
        }
    }

    fun startRequestAllDocumentsAtDirectory(path: String, parameter: Int) {
        if (!uiState.value.loading) {
            updateLoading()
            updateAuthOperationStatus(true)
            viewModelScope.launch {
                mapsRepository.requestAllDocumentsAtDirectory(path)
                    .addOnSuccessListener { querySnapshot ->
                        uiState.update {
                            when (parameter) {
                                0 -> it.copy(
                                    requestedDocumentsCollectionOfMaps = querySnapshot
                                )

                                1 -> it.copy(
                                    requestedDocumentsCurrentMap = querySnapshot
                                )

                                2 -> it.copy(
                                    requestedDocumentsVerification = querySnapshot
                                )

                                else -> it.copy()
                            }
                        }
                        updateLoading()
                        updateAuthOperationStatus(false)
                    }
                    .addOnFailureListener {
                        updateShowDialog()
                        updateLoading()
                    }
            }
        }
    }

    private fun startRequestToCreateStreetAtDirectory(path: String) {
        val data = hashMapOf(
            "points" to parsePoints(),
            "values" to uiState.value.values.slice(0..uiState.value.values.lastIndex - positionsCount),
            "additionalInfo" to uiState.value.additionInformation
        )

        viewModelScope.launch {
            mapsRepository.requestToCreateDocumentAtDirectory(path, uiState.value.streetName, data)
                .addOnSuccessListener {
                    updateRequestedDocument(false)
                }
                .addOnFailureListener {
                    updateShowDialog()
                }
        }
    }

    private fun startRequestToCreateDecoyAtDirectory(path: String) {
        viewModelScope.launch {
            mapsRepository.requestToCreateDocumentAtDirectory(
                path,
                uiState.value.mapName,
                hashMapOf()
            )
                .addOnSuccessListener {
                    viewModelScope.launch {
                        mapsRepository.requestToUploadImageAtDirectory(
                            path,
                            uiState.value.mapName,
                            uiState.value.newMapUri
                                ?: Uri.parse(context().getString(R.string.default_uri))
                        )
                            .addOnSuccessListener {
                                updateRequestedDocument(true)
                            }
                            .addOnFailureListener {
                                updateShowDialog()
                            }
                    }
                }
                .addOnFailureListener {
                    updateShowDialog()
                }
        }
    }

    fun startRequestToDeleteDocumentAtDirectory(path: String, name: String, docMode: Boolean) {
        viewModelScope.launch {
            mapsRepository.requestToDeleteDocumentAtDirectory(path, name)
                .addOnSuccessListener {
                    updateRequestedDocument(docMode)
                }
                .addOnFailureListener {
                    updateShowDialog()
                }
        }
    }

    fun startRequestToReplaceThisFileData(name: String) {
        val newFile =
            File(Environment.getExternalStorageDirectory().absolutePath + "/Download", "$name.png")
        if (newFile.exists()) {
            uiState.update {
                it.copy(
                    loadedMaps = uiState.value.loadedMaps + listOf(name)
                )
            }
            return
        }
        if (name !in uiState.value.corruptedMaps) {
            newFile.createNewFile()
            viewModelScope.launch {
                mapsRepository.requestToReplaceThisFileData(name, newFile)
                    .addOnSuccessListener {
                        updateRequestedDocument(true)
                        uiState.update {
                            it.copy(
                                loadedMaps = uiState.value.loadedMaps + listOf(name)
                            )
                        }
                    }
                    .addOnFailureListener {
                        uiState.update {
                            it.copy(
                                corruptedMaps = uiState.value.corruptedMaps + listOf(name)
                            )
                        }
                    }
            }
        }
    }

    fun startRequestSignIn(email: String, password: String) {
        if (email.isBlank()) {
            uiState.update {
                it.copy(
                    emailError = R.string.must_provide_value
                )
            }
            return
        }
        if (password.isBlank()) {
            uiState.update {
                it.copy(
                    emailError = R.string.must_provide_value
                )
            }
            return
        }

        updateAuthOperationStatus(true)
        viewModelScope.launch {
            mapsRepository.requestSignIn(email, password)
                .addOnSuccessListener {
                    updateAuthOperationStatus(false)
                    uiState.update {
                        it.copy(
                            email = "",
                            passwordError = null,
                            emailError = null,
                            password = ""
                        )
                    }
                }
                .addOnFailureListener {
                    updateAuthOperationStatus(false)
                    when (it.message) {
                        context().getString(R.string.email_badly_formatted) -> {
                            uiState.update {
                                it.copy(
                                    emailError = R.string.provide_correct_address,
                                    passwordError = null
                                )
                            }
                        }

                        context().getString(R.string.email_is_not_registered) -> {
                            viewModelScope.launch {
                                mapsRepository.requestSignUp(email, password)
                                    .addOnSuccessListener {
                                        viewModelScope.launch {
                                            mapsRepository.requestToCreateDocumentAtDirectory(
                                                "Users",
                                                email,
                                                hashMapOf("verified" to false)
                                            )
                                        }
                                        startRequestSignIn(email, password)
                                    }
                                    .addOnFailureListener {
                                        when (it.message) {
                                            context().getString(R.string.invalid_password) -> {
                                                uiState.update {
                                                    it.copy(
                                                        passwordError = R.string.at_least_6,
                                                        emailError = null
                                                    )
                                                }
                                            }

                                            else -> {
                                                uiState.update {
                                                    it.copy(
                                                        passwordError = R.string.uncathed_exception,
                                                        emailError = null
                                                    )
                                                }
                                            }
                                        }
                                    }
                            }
                        }

                        context().getString(R.string.wrong_password) -> {
                            uiState.update {
                                it.copy(
                                    passwordError = R.string.provide_correct_password,
                                    emailError = null
                                )
                            }
                        }

                        else -> {
                            uiState.update {
                                it.copy(
                                    passwordError = R.string.uncathed_exception,
                                    emailError = null
                                )
                            }
                        }
                    }
                }
        }
    }

    fun startSignOut() {
        val email = currentUser()!!.email!!
        mapsRepository.signOut()
        uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun context(): Context {
        return mapsRepository.providedContext()
    }

    fun currentUser(): FirebaseUser? {
        return mapsRepository.currentUser()
    }

    fun cardButtonOpenAction(name: String, navAction: (String) -> Unit) {
        navAction(Screens.CurrentMap.name)
        val path = Environment.getExternalStorageDirectory().absolutePath + "/Download"
        val exifInterface = ExifInterface("$path/$name.png")
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270 || orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            uiState.update {
                it.copy(
                    currentBitmapFile = File(path, "$name.png"),
                    currentMapId = name,
                    currentBitmapFileHeight = BitmapFactory.decodeFile("${path}/$name.png").width,
                    currentBitmapFileWidth = BitmapFactory.decodeFile("${path}/$name.png").height
                )
            }
        } else {
            uiState.update {
                it.copy(
                    currentBitmapFile = File(path, "$name.png"),
                    currentMapId = name,
                    currentBitmapFileHeight = BitmapFactory.decodeFile("${path}/$name.png").height,
                    currentBitmapFileWidth = BitmapFactory.decodeFile("${path}/$name.png").width
                )
            }
        }
    }

    fun emailIsVerified(email: String): Boolean {
        for (elem in uiState.value.requestedDocumentsVerification ?: listOf()) {
            if (elem.id == email && elem.get("verified") as Boolean)
                return true
        }
        return false
    }
}