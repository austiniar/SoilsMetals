package com.example.soilsmetals.ui.shared.functions

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalInput(
    label: String,
    value: String,
    action: (String) -> Unit,
    keyboardType: KeyboardType,
    isSingleLine: Boolean,
    isError: Boolean,
    size: Dp,
    placeholder: String,
    opaque: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = action,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = isSingleLine,
        isError = isError,
        placeholder = { Text(placeholder) },
        colors = if (!opaque) TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            placeholderColor = Color.LightGray
        ) else TextFieldDefaults.outlinedTextFieldColors(),
        modifier = modifier
            .width(size)
    )
}