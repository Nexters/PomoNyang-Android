package com.pomonyang.mohanyang.presentation.screen.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun ServerErrorScreen(
    onClickNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val suggestionUrl = "http://pf.kakao.com/_FvuAn"

    Box(

    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .size(240.dp)
                    .padding(bottom = MnSpacing.threeXLarge),
                painter = painterResource(id = R.drawable.ic_error), contentDescription = "server error"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MnSpacing.small)
            ) {
                Text(
                    style = MnTheme.typography.header4, color = MnTheme.textColorScheme.primary, text = stringResource(id = R.string.server_error_title)
                )
                Text(
                    style = MnTheme.typography.subBodyRegular.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MnTheme.textColorScheme.secondary,
                    text = stringResource(id = R.string.server_error_content)
                )
            }

            MnBoxButton(
                modifier = Modifier.width(200.dp),
                text = stringResource(id = R.string.move_to_home),
                onClick = onClickNavigateToHome,
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large,
                containerPadding = PaddingValues(top = 60.dp),
            )

            Text(
                modifier = Modifier
                    .padding(top = MnSpacing.large)
                    .clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(suggestionUrl)))
                    },
                style = MnTheme.typography.subBodyRegular.copy(
                    textDecoration = TextDecoration.Underline,
                ),
                color = MnTheme.textColorScheme.tertiary,
                text = stringResource(id = R.string.inquiry_service),
            )
        }
    }

}

@Preview
@Composable
fun PreviewServerErrorScreen() {
    MnTheme {
        ServerErrorScreen(onClickNavigateToHome = {})
    }
}
