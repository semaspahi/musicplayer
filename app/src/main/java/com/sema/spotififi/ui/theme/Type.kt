package com.sema.spotififi.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sema.spotififi.R


private val ProductSansFontFamily = FontFamily(
    Font(R.font.product_sans_regular, FontWeight.Medium),
)
val Typography = Typography(
    defaultFontFamily = ProductSansFontFamily,
    h1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        letterSpacing = 1.sp,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.em
    )
)