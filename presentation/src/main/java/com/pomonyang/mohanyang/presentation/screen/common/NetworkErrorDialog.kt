package com.pomonyang.mohanyang.presentation.screen.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog

@Composable
fun NetworkErrorDialog(
    onClickRefresh: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) = with(LocalContext.current) {
    MnDialog(
        modifier = modifier,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = true,
        ),
        title = getString(R.string.network_error_title),
        subTitle = getString(R.string.network_error_content),
        positiveButton = {
            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                text = getString(R.string.network_refresh),
                onClick = onClickRefresh,
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.medium,

            )
        },
        onDismissRequest = onDismissRequest,
    )
}
