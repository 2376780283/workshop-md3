package com.slay.workshopnative.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.slay.workshopnative.data.preferences.AppThemeMode
import com.slay.workshopnative.data.preferences.DEFAULT_APP_THEME_MODE

private val LightScheme =
    lightColorScheme(
        primary = green_primaryLight,
        onPrimary = green_onPrimaryLight,
        primaryContainer = green_primaryContainerLight,
        onPrimaryContainer = green_onPrimaryContainerLight,
        secondary = green_secondaryLight,
        onSecondary = green_onSecondaryLight,
        secondaryContainer = green_secondaryContainerLight,
        onSecondaryContainer = green_onSecondaryContainerLight,
        tertiary = green_tertiaryLight,
        onTertiary = green_onTertiaryLight,
        tertiaryContainer = green_tertiaryContainerLight,
        onTertiaryContainer = green_onTertiaryContainerLight,
        error = green_errorLight,
        onError = green_onErrorLight,
        errorContainer = green_errorContainerLight,
        onErrorContainer = green_onErrorContainerLight,
        background = green_backgroundLight,
        onBackground = green_onBackgroundLight,
        surface = green_surfaceLight,
        onSurface = green_onSurfaceLight,
        surfaceVariant = green_surfaceVariantLight,
        onSurfaceVariant = green_onSurfaceVariantLight,
        outline = green_outlineLight,
        outlineVariant = green_outlineVariantLight,
        scrim = green_scrimLight,
        inverseSurface = green_inverseSurfaceLight,
        inverseOnSurface = green_inverseOnSurfaceLight,
        inversePrimary = green_inversePrimaryLight,
        surfaceDim = green_surfaceDimLight,
        surfaceBright = green_surfaceBrightLight,
        surfaceContainerLowest = green_surfaceContainerLowestLight,
        surfaceContainerLow = green_surfaceContainerLowLight,
        surfaceContainer = green_surfaceContainerLight,
        surfaceContainerHigh = green_surfaceContainerHighLight,
        surfaceContainerHighest = green_surfaceContainerHighestLight,
    )

private val DarkScheme =
    darkColorScheme(
        primary = green_primaryDark,
        onPrimary = green_onPrimaryDark,
        primaryContainer = green_primaryContainerDark,
        onPrimaryContainer = green_onPrimaryContainerDark,
        secondary = green_secondaryDark,
        onSecondary = green_onSecondaryDark,
        secondaryContainer = green_secondaryContainerDark,
        onSecondaryContainer = green_onSecondaryContainerDark,
        tertiary = green_tertiaryDark,
        onTertiary = green_onTertiaryDark,
        tertiaryContainer = green_tertiaryContainerDark,
        onTertiaryContainer = green_onTertiaryContainerDark,
        error = green_errorDark,
        onError = green_onErrorDark,
        errorContainer = green_errorContainerDark,
        onErrorContainer = green_onErrorContainerDark,
        background = green_backgroundDark,
        onBackground = green_onBackgroundDark,
        surface = green_surfaceDark,
        onSurface = green_onSurfaceDark,
        surfaceVariant = green_surfaceVariantDark,
        onSurfaceVariant = green_onSurfaceVariantDark,
        outline = green_outlineDark,
        outlineVariant = green_outlineVariantDark,
        scrim = green_scrimDark,
        inverseSurface = green_inverseSurfaceDark,
        inverseOnSurface = green_inverseOnSurfaceDark,
        inversePrimary = green_inversePrimaryDark,
        surfaceDim = green_surfaceDimDark,
        surfaceBright = green_surfaceBrightDark,
        surfaceContainerLowest = green_surfaceContainerLowestDark,
        surfaceContainerLow = green_surfaceContainerLowDark,
        surfaceContainer = green_surfaceContainerDark,
        surfaceContainerHigh = green_surfaceContainerHighDark,
        surfaceContainerHighest = green_surfaceContainerHighestDark,
    )

@Composable
fun WorkshopNativeTheme(
    themeMode: AppThemeMode = DEFAULT_APP_THEME_MODE,
    content: @Composable () -> Unit,
) {
    val darkTheme =
        when (themeMode) {
            AppThemeMode.System -> isSystemInDarkTheme()
            AppThemeMode.Light -> false
            AppThemeMode.Dark -> true
        }

    val targetScheme = if (darkTheme) DarkScheme else LightScheme
    val animatedColorScheme = animateColorScheme(targetScheme)

    CompositionLocalProvider(LocalWorkshopDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = animatedColorScheme,
            typography = WorkshopTypography,
            content = content,
        )
    }
}

@Composable
fun animateColorScheme(targetScheme: ColorScheme): ColorScheme {
    val animationSpec = tween<androidx.compose.ui.graphics.Color>(durationMillis = 300)

    return targetScheme.copy(
        primary = animateColorAsState(targetScheme.primary, animationSpec).value,
        onPrimary = animateColorAsState(targetScheme.onPrimary, animationSpec).value,
        primaryContainer = animateColorAsState(targetScheme.primaryContainer, animationSpec).value,
        onPrimaryContainer = animateColorAsState(targetScheme.onPrimaryContainer, animationSpec).value,
        secondary = animateColorAsState(targetScheme.secondary, animationSpec).value,
        onSecondary = animateColorAsState(targetScheme.onSecondary, animationSpec).value,
        secondaryContainer = animateColorAsState(targetScheme.secondaryContainer, animationSpec).value,
        onSecondaryContainer = animateColorAsState(targetScheme.onSecondaryContainer, animationSpec).value,
        tertiary = animateColorAsState(targetScheme.tertiary, animationSpec).value,
        onTertiary = animateColorAsState(targetScheme.onTertiary, animationSpec).value,
        tertiaryContainer = animateColorAsState(targetScheme.tertiaryContainer, animationSpec).value,
        onTertiaryContainer = animateColorAsState(targetScheme.onTertiaryContainer, animationSpec).value,
        error = animateColorAsState(targetScheme.error, animationSpec).value,
        onError = animateColorAsState(targetScheme.onError, animationSpec).value,
        errorContainer = animateColorAsState(targetScheme.errorContainer, animationSpec).value,
        onErrorContainer = animateColorAsState(targetScheme.onErrorContainer, animationSpec).value,
        background = animateColorAsState(targetScheme.background, animationSpec).value,
        onBackground = animateColorAsState(targetScheme.onBackground, animationSpec).value,
        surface = animateColorAsState(targetScheme.surface, animationSpec).value,
        onSurface = animateColorAsState(targetScheme.onSurface, animationSpec).value,
        surfaceVariant = animateColorAsState(targetScheme.surfaceVariant, animationSpec).value,
        onSurfaceVariant = animateColorAsState(targetScheme.onSurfaceVariant, animationSpec).value,
        outline = animateColorAsState(targetScheme.outline, animationSpec).value,
        outlineVariant = animateColorAsState(targetScheme.outlineVariant, animationSpec).value,
        scrim = animateColorAsState(targetScheme.scrim, animationSpec).value,
        inverseSurface = animateColorAsState(targetScheme.inverseSurface, animationSpec).value,
        inverseOnSurface = animateColorAsState(targetScheme.inverseOnSurface, animationSpec).value,
        inversePrimary = animateColorAsState(targetScheme.inversePrimary, animationSpec).value,
        surfaceDim = animateColorAsState(targetScheme.surfaceDim, animationSpec).value,
        surfaceBright = animateColorAsState(targetScheme.surfaceBright, animationSpec).value,
        surfaceContainerLowest = animateColorAsState(targetScheme.surfaceContainerLowest, animationSpec).value,
        surfaceContainerLow = animateColorAsState(targetScheme.surfaceContainerLow, animationSpec).value,
        surfaceContainer = animateColorAsState(targetScheme.surfaceContainer, animationSpec).value,
        surfaceContainerHigh = animateColorAsState(targetScheme.surfaceContainerHigh, animationSpec).value,
        surfaceContainerHighest = animateColorAsState(targetScheme.surfaceContainerHighest, animationSpec).value,
    )
}
