package com.slay.workshopnative.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.slay.workshopnative.data.preferences.AppThemeMode
import com.slay.workshopnative.data.preferences.DEFAULT_APP_THEME_MODE

private val LightScheme = lightColorScheme(
    primary = purple_primaryLight,
                    onPrimary = purple_onPrimaryLight,
                    primaryContainer = purple_primaryContainerLight,
                    onPrimaryContainer = purple_onPrimaryContainerLight,
                    secondary = purple_secondaryLight,
                    onSecondary = purple_onSecondaryLight,
                    secondaryContainer = purple_secondaryContainerLight,
                    onSecondaryContainer = purple_onSecondaryContainerLight,
                    tertiary = purple_tertiaryLight,
                    onTertiary = purple_onTertiaryLight,
                    tertiaryContainer = purple_tertiaryContainerLight,
                    onTertiaryContainer = purple_onTertiaryContainerLight,
                    error = purple_errorLight,
                    onError = purple_onErrorLight,
                    errorContainer = purple_errorContainerLight,
                    onErrorContainer = purple_onErrorContainerLight,
                    background = purple_backgroundLight,
                    onBackground = purple_onBackgroundLight,
                    surface = purple_surfaceLight,
                    onSurface = purple_onSurfaceLight,
                    surfaceVariant = purple_surfaceVariantLight,
                    onSurfaceVariant = purple_onSurfaceVariantLight,
                    outline = purple_outlineLight,
                    outlineVariant = purple_outlineVariantLight,
                    scrim = purple_scrimLight,
                    inverseSurface = purple_inverseSurfaceLight,
                    inverseOnSurface = purple_inverseOnSurfaceLight,
                    inversePrimary = purple_inversePrimaryLight,
                    surfaceDim = purple_surfaceDimLight,
                    surfaceBright = purple_surfaceBrightLight,
                    surfaceContainerLowest = purple_surfaceContainerLowestLight,
                    surfaceContainerLow = purple_surfaceContainerLowLight,
                    surfaceContainer = purple_surfaceContainerLight,
                    surfaceContainerHigh = purple_surfaceContainerHighLight,
                    surfaceContainerHighest = purple_surfaceContainerHighestLight,
)

private val DarkScheme = darkColorScheme(
          primary = purple_primaryDark,
                    onPrimary = purple_onPrimaryDark,
                    primaryContainer = purple_primaryContainerDark,
                    onPrimaryContainer = purple_onPrimaryContainerDark,
                    secondary = purple_secondaryDark,
                    onSecondary = purple_onSecondaryDark,
                    secondaryContainer = purple_secondaryContainerDark,
                    onSecondaryContainer = purple_onSecondaryContainerDark,
                    tertiary = purple_tertiaryDark,
                    onTertiary = purple_onTertiaryDark,
                    tertiaryContainer = purple_tertiaryContainerDark,
                    onTertiaryContainer = purple_onTertiaryContainerDark,
                    error = purple_errorDark,
                    onError = purple_onErrorDark,
                    errorContainer = purple_errorContainerDark,
                    onErrorContainer = purple_onErrorContainerDark,
                    background = purple_backgroundDark,
                    onBackground = purple_onBackgroundDark,
                    surface = purple_surfaceDark,
                    onSurface = purple_onSurfaceDark,
                    surfaceVariant = purple_surfaceVariantDark,
                    onSurfaceVariant = purple_onSurfaceVariantDark,
                    outline = purple_outlineDark,
                    outlineVariant = purple_outlineVariantDark,
                    scrim = purple_scrimDark,
                    inverseSurface = purple_inverseSurfaceDark,
                    inverseOnSurface = purple_inverseOnSurfaceDark,
                    inversePrimary = purple_inversePrimaryDark,
                    surfaceDim = purple_surfaceDimDark,
                    surfaceBright = purple_surfaceBrightDark,
                    surfaceContainerLowest = purple_surfaceContainerLowestDark,
                    surfaceContainerLow = purple_surfaceContainerLowDark,
                    surfaceContainer = purple_surfaceContainerDark,
                    surfaceContainerHigh = purple_surfaceContainerHighDark,
                    surfaceContainerHighest = purple_surfaceContainerHighestDark,
)

@Composable
fun WorkshopNativeTheme(
    themeMode: AppThemeMode = DEFAULT_APP_THEME_MODE,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.System -> isSystemInDarkTheme()
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }
    CompositionLocalProvider(LocalWorkshopDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            typography = WorkshopTypography,
            content = content,
        )
    }
}
