package com.pomonyang.mohanyang.presentation.screen.mypage.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.model.cat.CatType
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary)
    ) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = onBackClick,
                    iconResourceId = R.drawable.ic_chevron_left
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
            CatRive(
                modifier = Modifier.fillMaxWidth(),
                tooltipMessage = stringResource(id = R.string.cat_profile_tooltip),
                riveResource = R.raw.cat_motion_transparent
            )
            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall)
            ) {
                Text(
                    text = CatType.CHEESE.name,
                    style = MnTheme.typography.header4,
                    color = MnTheme.textColorScheme.tertiary
                )
                MnMediumIcon(resourceId = R.drawable.ic_pen, tint = MnTheme.iconColorScheme.primary)
            }
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
