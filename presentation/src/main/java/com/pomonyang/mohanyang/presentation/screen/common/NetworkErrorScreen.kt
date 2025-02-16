package com.pomonyang.mohanyang.presentation.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun NetworkErrorScreen(
    onClickRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MnSpacing.threeXLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(240.dp),
                    painter = painterResource(id = R.drawable.ic_internet),
                    contentDescription = "network error",
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
                ) {
                    Text(
                        style = MnTheme.typography.header4,
                        color = MnTheme.textColorScheme.primary,
                        text = stringResource(id = R.string.network_error_title),
                    )
                    Text(
                        style = MnTheme.typography.subBodyRegular.copy(
                            textAlign = TextAlign.Center,
                        ),
                        color = MnTheme.textColorScheme.secondary,
                        text = stringResource(id = R.string.network_error_content),
                    )
                }
            }

            MnBoxButton(
                modifier = Modifier.width(200.dp),
                text = stringResource(id = R.string.network_refresh),
                onClick = onClickRetry,
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large,
                containerPadding = PaddingValues(top = 60.dp),
            )
        }
    }
}

@Preview
@Composable
fun PreviewNetworkErrorScreen() {
    MnTheme {
        NetworkErrorScreen(onClickRetry = {})
    }
}
