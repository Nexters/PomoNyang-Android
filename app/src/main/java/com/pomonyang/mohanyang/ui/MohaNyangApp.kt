package com.pomonyang.mohanyang.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.navigation.MohaNyangNavHost
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.toast.MnToastSnackbarHost
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import kotlinx.coroutines.launch

@Composable
internal fun MohaNyangApp(
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier
) {
    MohaNyangApp(
        modifier = modifier,
        mohaNyangAppState = mohaNyangAppState,
        snackbarHostState = remember { SnackbarHostState() }
    )
}

@Composable
private fun MohaNyangApp(
    mohaNyangAppState: MohaNyangAppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var snackbarIconRes by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = modifier,
        containerColor = MnTheme.backgroundColorScheme.primary,
        snackbarHost = { MnToastSnackbarHost(hostState = snackbarHostState, leadingIconResourceId = snackbarIconRes) }
    ) { innerPadding ->
        val isOffline by mohaNyangAppState.isOffline.collectAsStateWithLifecycle()
        val showSnackbar: (String, Int?) -> Unit = remember {
            { message, iconRes ->
                snackbarIconRes = iconRes
                mohaNyangAppState.coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = Short
                    )
                }
            }
        }

        if (isOffline) {
            OfflinePopup()
        }
        MohaNyangNavHost(
            onShowSnackbar = showSnackbar,
            mohaNyangAppState = mohaNyangAppState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun OfflinePopup(
    modifier: Modifier = Modifier
) {
    Popup(alignment = Alignment.TopCenter) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(MnRadius.max),
                    clip = true
                )
                .background(
                    color = MnColor.White,
                    shape = RoundedCornerShape(MnRadius.max)
                )
                .padding(horizontal = MnSpacing.xLarge, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            MnXSmallIcon(resourceId = com.mohanyang.presentation.R.drawable.ic_null)
            Text(
                text = stringResource(R.string.offline_mode_message),
                style = MnTheme.typography.bodySemiBold,
                color = MnTheme.textColorScheme.secondary
            )
        }
    }
}

@ThemePreviews
@Composable
private fun NetworkErrorMessagePreview() {
    OfflinePopup()
}
