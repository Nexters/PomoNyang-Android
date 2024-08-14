package com.pomonyang.mohanyang.presentation.screen.mypage.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.domain.model.cat.CatType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.tooltip
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews

@Composable
internal fun CatProfileRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CatProfileScreen(
        onBackClick = onBackClick,
        onCatChangeClick = {},
        modifier = modifier
    )
}

@Composable
private fun CatProfileScreen(
    onBackClick: () -> Unit,
    onCatChangeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = onBackClick,
                    iconResourceId = R.drawable.ic_null
                )
            },
            content = {
                Text(
                    text = "나의 고양이",
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary
                )
            }
        )

        Column(modifier = Modifier.padding(horizontal = MnSpacing.xLarge)) {
            Spacer(modifier = Modifier.weight(1f))
            CatRiveBox(catType = CatType.CHEESE)
            Spacer(modifier = Modifier.weight(1f))
            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                containerPadding = PaddingValues(bottom = MnSpacing.small),
                text = "고양이 바꾸기",
                onClick = { /*TODO*/ },
                colors = MnBoxButtonColorType.secondary,
                styles = MnBoxButtonStyles.large
            )
        }
    }
}

@Composable
fun CatRiveBox(
    modifier: Modifier = Modifier,
    catType: CatType
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    color = MnTheme.backgroundColorScheme.secondary
                )
                .tooltip("사냥놀이를 하고 싶다냥"),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "image")
        }
        Row(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.medium
            ),
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
        ) {
            Text(
                text = catType.kor,
                style = MnTheme.typography.header4,
                color = MnTheme.textColorScheme.secondary
            )
            MnMediumIcon(resourceId = R.drawable.ic_null)
        }
    }
}

@DevicePreviews
@Composable
fun PreviewCatProfileScreen() {
    MnTheme {
        CatProfileScreen(
            onBackClick = {},
            onCatChangeClick = {}
        )
    }
}
