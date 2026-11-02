package me.ash.reader.ui.page.home.feeds

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import me.ash.reader.R
import me.ash.reader.ui.component.base.FeedbackIconButton
import me.ash.reader.ui.icons.RokkuIcons

@Composable
fun FeedsMoreMenu(
    showBadge: Boolean,
    versionName: String,
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    FeedbackIconButton(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = stringResource(R.string.more),
        tint = MaterialTheme.colorScheme.onSurface,
        showBadge = showBadge,
    ) {
        expanded = true
    }
    if (expanded) {
        OverflowMenu(onDismiss = { expanded = false }) {
            OverflowMenuItem(
                icon = RokkuIcons.Settings,
                title = stringResource(R.string.settings),
                onClick = {
                    expanded = false
                    navigateToSettings()
                },
            )
            OverflowMenuItem(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.about),
                subtitle = "v$versionName",
                onClick = {
                    expanded = false
                    navigateToAbout()
                },
            )
        }
    }
}

@Composable
private fun OverflowMenu(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val blurRadius = with(LocalDensity.current) { 10.dp.roundToPx() }
    val background = MaterialTheme.colorScheme.background
    val accent = MaterialTheme.colorScheme.primary

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect {
            if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                window.attributes = window.attributes.also { it.blurBehindRadius = blurRadius }
            }
            window?.setDimAmount(0.25f)
        }
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    )
        ) {
            Surface(
                modifier =
                    Modifier.align(Alignment.TopEnd)
                        .padding(top = topInset + 16.dp, end = 14.dp, start = 24.dp)
                        .widthIn(min = 250.dp),
                shape = RoundedCornerShape(12.dp),
                color = lerp(background, accent, 0.075f),
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max).padding(vertical = 6.dp),
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun OverflowMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .heightIn(min = 48.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
