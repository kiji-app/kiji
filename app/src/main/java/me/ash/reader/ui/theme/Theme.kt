package me.ash.reader.ui.theme

import android.os.Build
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import me.ash.reader.infrastructure.preference.LocalAmoledDarkTheme
import me.ash.reader.infrastructure.preference.LocalBasicFonts
import me.ash.reader.infrastructure.preference.LocalThemeIndex
import me.ash.reader.ui.theme.palette.LocalTonalPalettes
import me.ash.reader.ui.theme.palette.TonalPalettes
import me.ash.reader.ui.theme.palette.TonalPalettes.Companion.toTonalPalettes
import me.ash.reader.ui.theme.palette.core.ProvideZcamViewingConditions
import me.ash.reader.ui.theme.palette.dynamic.extractTonalPalettesFromUserWallpaper
import me.ash.reader.ui.theme.palette.dynamicDarkColorScheme
import me.ash.reader.ui.theme.palette.dynamicLightColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    useDarkTheme: Boolean,
    wallpaperPalettes: List<TonalPalettes> = extractTonalPalettesFromUserWallpaper(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current

    LaunchedEffect(useDarkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (useDarkTheme) {
                view.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                view.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        }
    }

    val themeIndex = LocalThemeIndex.current

    val namedTheme = NamedThemes.getOrNull(themeIndex - NamedThemeIndexOffset)
    val namedColors = namedTheme?.let { if (useDarkTheme) it.dark else it.light }

    // Composable calls stay unconditional: flipping a branch here would rebuild the whole app and reset navigation.
    val namedPalettes = (namedColors?.primary ?: Color.Transparent).toTonalPalettes()
    val wallpaperIndex =
        if (themeIndex >= wallpaperPalettes.size) {
            when {
                wallpaperPalettes.size == 5 -> 0
                wallpaperPalettes.size > 5 -> 5
                else -> 0
            }
        } else {
            themeIndex
        }
    val tonalPalettes = if (namedColors != null) namedPalettes else wallpaperPalettes[wallpaperIndex]

    ProvideZcamViewingConditions {
        CompositionLocalProvider(
            LocalTonalPalettes provides tonalPalettes.apply { Preparing() },
            LocalTextStyle provides LocalTextStyle.current.applyTextDirection(),
        ) {
            val amoled = LocalAmoledDarkTheme.current.value
            val lightColors = dynamicLightColorScheme()
            val darkColors = dynamicDarkColorScheme()
            val resolvedNamedColors = namedColors?.fillUnspecified(if (useDarkTheme) darkColors else lightColors)
            MaterialTheme(
                motionScheme = MotionScheme.expressive(),
                colorScheme =
                    when {
                        resolvedNamedColors == null -> if (useDarkTheme) darkColors else lightColors
                        useDarkTheme && amoled ->
                            resolvedNamedColors.copy(background = Color.Black, surface = Color.Black)
                        else -> resolvedNamedColors
                    },
                typography =
                    LocalBasicFonts.current
                        .asTypography(LocalContext.current)
                        .applyTextDirection(),
                shapes = Shapes,
                content = content,
            )
        }
    }
}

// Roles the Rokku theme doesn't define (always the Fixed ones) come from the primary-derived palette instead of Material's purple/pink baseline.
private fun ColorScheme.fillUnspecified(base: ColorScheme): ColorScheme {
    fun Color.or(fallback: Color) = if (isSpecified) this else fallback
    return copy(
        primary = primary.or(base.primary),
        onPrimary = onPrimary.or(base.onPrimary),
        primaryContainer = primaryContainer.or(base.primaryContainer),
        onPrimaryContainer = onPrimaryContainer.or(base.onPrimaryContainer),
        inversePrimary = inversePrimary.or(base.inversePrimary),
        secondary = secondary.or(base.secondary),
        onSecondary = onSecondary.or(base.onSecondary),
        secondaryContainer = secondaryContainer.or(base.secondaryContainer),
        onSecondaryContainer = onSecondaryContainer.or(base.onSecondaryContainer),
        tertiary = tertiary.or(base.tertiary),
        onTertiary = onTertiary.or(base.onTertiary),
        tertiaryContainer = tertiaryContainer.or(base.tertiaryContainer),
        onTertiaryContainer = onTertiaryContainer.or(base.onTertiaryContainer),
        background = background.or(base.background),
        onBackground = onBackground.or(base.onBackground),
        surface = surface.or(base.surface),
        onSurface = onSurface.or(base.onSurface),
        surfaceVariant = surfaceVariant.or(base.surfaceVariant),
        onSurfaceVariant = onSurfaceVariant.or(base.onSurfaceVariant),
        surfaceTint = surfaceTint.or(base.surfaceTint),
        inverseSurface = inverseSurface.or(base.inverseSurface),
        inverseOnSurface = inverseOnSurface.or(base.inverseOnSurface),
        error = error.or(base.error),
        onError = onError.or(base.onError),
        errorContainer = errorContainer.or(base.errorContainer),
        onErrorContainer = onErrorContainer.or(base.onErrorContainer),
        outline = outline.or(base.outline),
        outlineVariant = outlineVariant.or(base.outlineVariant),
        scrim = scrim.or(base.scrim),
        surfaceBright = surfaceBright.or(base.surfaceBright),
        surfaceDim = surfaceDim.or(base.surfaceDim),
        surfaceContainer = surfaceContainer.or(base.surfaceContainer),
        surfaceContainerHigh = surfaceContainerHigh.or(base.surfaceContainerHigh),
        surfaceContainerHighest = surfaceContainerHighest.or(base.surfaceContainerHighest),
        surfaceContainerLow = surfaceContainerLow.or(base.surfaceContainerLow),
        surfaceContainerLowest = surfaceContainerLowest.or(base.surfaceContainerLowest),
        primaryFixed = base.primaryFixed,
        primaryFixedDim = base.primaryFixedDim,
        onPrimaryFixed = base.onPrimaryFixed,
        onPrimaryFixedVariant = base.onPrimaryFixedVariant,
        secondaryFixed = base.secondaryFixed,
        secondaryFixedDim = base.secondaryFixedDim,
        onSecondaryFixed = base.onSecondaryFixed,
        onSecondaryFixedVariant = base.onSecondaryFixedVariant,
        tertiaryFixed = base.tertiaryFixed,
        tertiaryFixedDim = base.tertiaryFixedDim,
        onTertiaryFixed = base.onTertiaryFixed,
        onTertiaryFixedVariant = base.onTertiaryFixedVariant,
    )
}
