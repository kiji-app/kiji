package me.ash.reader.ui.component.base

import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WavyProgressIndicator(
    modifier: Modifier = Modifier,
    progress: (() -> Float)? = null,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    if (progress == null) {
        CircularWavyProgressIndicator(modifier = modifier, color = color, amplitude = 0f)
    } else {
        CircularWavyProgressIndicator(progress = progress, modifier = modifier, color = color)
    }
}
