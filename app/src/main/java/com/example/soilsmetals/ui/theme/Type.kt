package com.example.soilsmetals.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.soilsmetals.R

val Kosugimaru = FontFamily(
    Font(R.font.kosugimaru_regular, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    // main title
    headlineSmall = TextStyle(
        fontFamily = Kosugimaru,
        fontWeight = FontWeight.Light,
        fontSize = 30.sp,
        lineHeight = 40.sp,
        letterSpacing = (-10).sp,
        textAlign = TextAlign.Center
    ),
    // english
        headlineMedium = TextStyle(
            fontFamily = Kosugimaru,
            fontWeight = FontWeight.Light,
            fontSize = 30.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
            textAlign = TextAlign.Center
        ),
    // headers
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Justify
    ),
    // errors
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),
    // names
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    )
)