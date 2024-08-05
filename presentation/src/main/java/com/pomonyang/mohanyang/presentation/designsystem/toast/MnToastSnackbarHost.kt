package com.pomonyang.mohanyang.presentation.designsystem.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MnToastSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    leadingIconResourceId: Int? = null
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.alpha(0.9f)
    ) { data ->
        Card(
            shape = RoundedCornerShape(MnRadius.small),
            colors = CardDefaults.cardColors(
                containerColor = MnTheme.backgroundColorScheme.inverse,
                contentColor = MnColor.White
            )
        ) {
            Row(
                modifier = Modifier.padding(MnSpacing.large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIconResourceId != null) {
                    MnMediumIcon(resourceId = leadingIconResourceId, tint = MnTheme.iconColorScheme.disabled)
                    Spacer(modifier = Modifier.padding(end = MnSpacing.small))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.visuals.message,
                        style = MnTheme.typography.subBodySemiBold
                    )
                }

                if (data.visuals.actionLabel != null) {
                    Spacer(modifier = Modifier.padding(start = MnSpacing.small))
                    Text(
                        modifier = Modifier.noRippleClickable {
                            data.performAction()
                        },
                        text = data.visuals.actionLabel!!,
                        style = MnTheme.typography.subBodySemiBold
                    )
                }
            }
        }
    }
}

suspend fun showMnSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    actionPerformed: () -> Unit = {},
    dismissed: () -> Unit = {}
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )

    when (result) {
        SnackbarResult.ActionPerformed -> actionPerformed()
        SnackbarResult.Dismissed -> dismissed()
    }
}

@ThemePreviews
@Composable
fun PreviewToast() {
    MnTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        val scope = rememberCoroutineScope()

        Scaffold(

            snackbarHost = {
                MnToastSnackbarHost(
                    hostState = snackbarHostState,
                    leadingIconResourceId = R.drawable.ic_null
                )
            }
        ) { _ ->
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .fillMaxSize()
            ) {
                Button(onClick = {
                    scope.launch {
                        showMnSnackbar(snackbarHostState = snackbarHostState, message = "안녕나는고양이야나한테말하기전에확인", actionLabel = "확인", dismissed = {
                            Timber.d("Snackbar Dismissed")
                        }, actionPerformed = {
                            Timber.d("Snackbar Action")
                        })
                    }
                }) { Text(text = "show Toast") }
            }
        }
    }
}
