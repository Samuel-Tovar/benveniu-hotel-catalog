package com.example.hotelcatalog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


// =====================================================
// TEMA CLARO - BENVENIU HOTEL
// =====================================================

private val LightColorScheme = lightColorScheme(

    primary = HotelBrown,
    onPrimary = HotelWhite,

    secondary = HotelBrownSecondary,
    onSecondary = HotelWhite,

    tertiary = HotelGold,
    onTertiary = HotelBrownDeep,

    background = HotelBackground,
    onBackground = HotelBrownDeep,

    surface = HotelSurface,
    onSurface = HotelBrownDeep,

    surfaceVariant = HotelCard,
    onSurfaceVariant = HotelBrownDark,

    error = HotelUnavailable,
    onError = HotelWhite
)


// =====================================================
// TEMA OSCURO - BENVENIU HOTEL
// =====================================================

private val DarkColorScheme = darkColorScheme(

    primary = HotelCard,
    onPrimary = HotelBrownDeep,

    secondary = HotelTextLight,
    onSecondary = HotelBrownDeep,

    tertiary = HotelGold,
    onTertiary = HotelBrownDeep,

    background = HotelBrownDeep,
    onBackground = HotelTextLight,

    surface = HotelBrownDark,
    onSurface = HotelTextLight,

    surfaceVariant = HotelBrown,
    onSurfaceVariant = HotelTextLight,

    error = HotelUnavailable,
    onError = HotelWhite
)


// =====================================================
// TEMA PRINCIPAL
// =====================================================

@Composable
fun HotelCatalogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {

        DarkColorScheme

    } else {

        LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}