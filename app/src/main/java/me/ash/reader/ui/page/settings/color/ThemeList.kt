package me.ash.reader.ui.page.settings.color

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.ash.reader.R
import me.ash.reader.infrastructure.preference.CustomPrimaryColorPreference
import me.ash.reader.infrastructure.preference.LocalDarkTheme
import me.ash.reader.infrastructure.preference.ThemeIndexPreference
import me.ash.reader.ui.component.base.TextFieldDialog
import me.ash.reader.ui.theme.NamedThemeIndexOffset
import me.ash.reader.ui.theme.NamedThemes
import me.ash.reader.ui.theme.palette.TonalPalettes
import me.ash.reader.ui.theme.palette.checkColorHex
import me.ash.reader.ui.theme.palette.onDark

private val CardWidth = 104.dp
private val CardSpacing = 8.dp
private const val FirstWallpaperIndex = 5
private const val CustomIndex = 4

private class ThemeOption(
    val index: Int,
    val name: String,
    val surface: Color,
    val primary: Color,
    val container: Color,
)

@Composable
private fun TonalPalettes.toOption(index: Int, name: String) =
    ThemeOption(
        index = index,
        name = name,
        surface = (this neutral 99) onDark (this neutral 10),
        primary = (this primary 40) onDark (this primary 80),
        container = (this secondary 90) onDark (this secondary 30),
    )

@Composable
fun ThemeList(
    context: Context,
    themeIndex: Int,
    palettes: List<TonalPalettes>,
    customPrimaryColor: String,
) {
    val scope = rememberCoroutineScope()
    val isDark = LocalDarkTheme.current.isDarkTheme()
    var customDialogVisible by remember { mutableStateOf(false) }
    var customColorValue by remember { mutableStateOf(customPrimaryColor) }

    val wallpaperNames =
        listOf(
            stringResource(if (isDark) R.string.theme_dynamic_dark else R.string.theme_dynamic_light),
            stringResource(R.string.theme_wallpaper_secondary),
            stringResource(R.string.theme_wallpaper_tertiary),
        )
    val basicNames =
        listOf(
            stringResource(R.string.theme_blue),
            stringResource(R.string.theme_pink),
            stringResource(R.string.theme_purple),
            stringResource(R.string.theme_yellow),
            stringResource(R.string.theme_custom),
        )

    val wallpaperOptions =
        palettes.drop(FirstWallpaperIndex).mapIndexed { i, palette ->
            palette.toOption(FirstWallpaperIndex + i, wallpaperNames[i.coerceAtMost(2)])
        }
    val namedOptions =
        NamedThemes.mapIndexed { i, theme ->
            val colors = if (isDark) theme.dark else theme.light
            ThemeOption(
                index = NamedThemeIndexOffset + i,
                name = stringResource(if (isDark) theme.darkName else theme.lightName),
                surface = colors.surface,
                primary = colors.primary,
                container = colors.secondaryContainer.takeOrElse { colors.primary },
            )
        }
    val basicOptions =
        palettes.take(FirstWallpaperIndex).mapIndexed { i, palette -> palette.toOption(i, basicNames[i]) }
    val options = wallpaperOptions + namedOptions + basicOptions

    val selectedIndex =
        if (options.any { it.index == themeIndex }) themeIndex
        else options.firstOrNull()?.index ?: themeIndex

    val scrollState = rememberScrollState()
    val cardStep = with(LocalDensity.current) { (CardWidth + CardSpacing).toPx() }
    LaunchedEffect(Unit) {
        scrollState.scrollTo((options.indexOfFirst { it.index == selectedIndex } * cardStep).toInt())
    }

    Row(
        modifier =
            Modifier.horizontalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(CardSpacing),
    ) {
        options.forEach { option ->
            val selected = option.index == selectedIndex
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                border =
                    if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier =
                    Modifier.width(CardWidth).fillMaxHeight().clip(RoundedCornerShape(16.dp)).clickable {
                        if (option.index == CustomIndex) {
                            customColorValue = customPrimaryColor
                            customDialogVisible = true
                        } else {
                            ThemeIndexPreference.put(context, scope, option.index)
                        }
                    },
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(option.surface),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(Modifier.size(14.dp).clip(CircleShape).background(option.primary))
                            Box(Modifier.size(14.dp).clip(CircleShape).background(option.container))
                        }
                    }
                    Text(
                        text = option.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                    )
                }
            }
        }
    }

    TextFieldDialog(
        visible = customDialogVisible,
        title = stringResource(R.string.primary_color),
        icon = Icons.Outlined.Palette,
        value = customColorValue,
        placeholder = stringResource(R.string.primary_color_hint),
        onValueChange = { customColorValue = it },
        onDismissRequest = { customDialogVisible = false },
        onConfirm = {
            it.checkColorHex()?.let { hex ->
                CustomPrimaryColorPreference.put(context, scope, hex)
                ThemeIndexPreference.put(context, scope, CustomIndex)
                customDialogVisible = false
            }
        },
    )
}
