package me.ash.reader.ui.page.about

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.text.DateFormat
import java.time.Instant
import java.util.Date
import me.ash.reader.BuildConfig
import me.ash.reader.R
import me.ash.reader.infrastructure.preference.OpenLinkPreference
import me.ash.reader.ui.component.base.DisplayText
import me.ash.reader.ui.component.base.FeedbackIconButton
import me.ash.reader.ui.component.base.RYScaffold
import me.ash.reader.ui.ext.DataStoreKey
import me.ash.reader.ui.ext.dataStore
import me.ash.reader.ui.ext.getCurrentVersion
import me.ash.reader.ui.ext.isGitHub
import me.ash.reader.ui.ext.openURL
import me.ash.reader.ui.ext.put
import me.ash.reader.ui.ext.showToast
import me.ash.reader.ui.page.settings.SettingItem
import me.ash.reader.ui.page.settings.tips.UpdateDialog
import me.ash.reader.ui.page.settings.tips.UpdateViewModel
import me.ash.reader.ui.theme.palette.onLight

@Composable
fun AboutPage(
    updateViewModel: UpdateViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navigateToLicenseList: () -> Unit,
) {
    val context = LocalContext.current
    val version = context.getCurrentVersion().toString()
    val buildTime =
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date.from(Instant.parse(BuildConfig.BUILD_TIME)))

    fun openLink(url: String) = context.openURL(url, OpenLinkPreference.AutoPreferCustomTabs)

    RYScaffold(
        containerColor =
            MaterialTheme.colorScheme.surface onLight MaterialTheme.colorScheme.inverseOnSurface,
        navigationIcon = {
            FeedbackIconButton(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface,
                onClick = onBack,
            )
        },
        content = {
            LazyColumn {
                item { DisplayText(text = stringResource(R.string.about), desc = "") }
                item {
                    SettingItem(
                        title = stringResource(R.string.whats_new_this_release),
                        onClick = { openLink(context.getString(R.string.release_notes_link)) },
                    )
                }
                if (isGitHub) {
                    item {
                        SettingItem(
                            title = stringResource(R.string.check_for_updates),
                            onClick = {
                                updateViewModel.checkUpdate(
                                    {
                                        context.showToast(context.getString(R.string.checking_updates))
                                        context.dataStore.put(DataStoreKey.skipVersionNumber, "")
                                    },
                                    {
                                        if (!it) {
                                            context.showToast(
                                                context.getString(R.string.is_latest_version)
                                            )
                                        }
                                    },
                                )
                            },
                        )
                    }
                }
                item {
                    SettingItem(
                        title = stringResource(R.string.version),
                        desc = version,
                        onClick = {
                            val appInfo = context.getString(R.string.app_info)
                            val deviceInfo =
                                "${context.getString(R.string.read_you)} $version\n" +
                                    "Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})\n" +
                                    "${Build.MANUFACTURER} ${Build.MODEL}"
                            context
                                .getSystemService<ClipboardManager>()
                                ?.setPrimaryClip(ClipData.newPlainText(appInfo, deviceInfo))
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                context.showToast(
                                    context.getString(R.string.copied_to_clipboard, appInfo)
                                )
                            }
                        },
                    )
                }
                item {
                    SettingItem(
                        title = stringResource(R.string.build_time),
                        desc = buildTime,
                        onClick = {},
                    )
                }
                item {
                    HorizontalDivider()
                    SettingItem(
                        title = stringResource(R.string.help_translate),
                        onClick = { openLink(context.getString(R.string.translatable_url)) },
                    )
                }
                item {
                    SettingItem(
                        title = stringResource(R.string.open_source_licenses),
                        onClick = navigateToLicenseList,
                    )
                }
                item {
                    SettingItem(
                        title = stringResource(R.string.based_on_read_you),
                        onClick = { openLink(context.getString(R.string.read_you_link)) },
                    )
                }
                item {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { openLink(context.getString(R.string.github_link)) }
                                .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_github),
                            contentDescription = "GitHub",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        },
    )

    UpdateDialog()
}
