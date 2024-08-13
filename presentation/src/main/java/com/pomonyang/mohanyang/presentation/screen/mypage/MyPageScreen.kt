package com.pomonyang.mohanyang.presentation.screen.mypage

import MnToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.mypage.component.FocusStatisticBox
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun MyPageScreen(
    isOffline: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = { /*TODO*/ },
                    iconResourceId = R.drawable.ic_null
                )
            },
            content = {
                Text(
                    text = "마이페이지",
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary
                )
            }
        )

        Column(
            modifier = Modifier.padding(
                horizontal = MnSpacing.xLarge,
                vertical = MnSpacing.medium
            ),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.medium)
        ) {
            ProfileBox()
            FocusStatisticBox(isOffline = isOffline)
            NotificationBox()
            SuggestionBox()
        }
    }
}

@Composable
fun ProfileBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small)
            )
            .padding(MnSpacing.xLarge)
            .noRippleClickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "나의 고양이",
                    color = MnTheme.textColorScheme.tertiary,
                    style = MnTheme.typography.subBodyRegular
                )
                Text(
                    text = "이이오",
                    color = MnTheme.textColorScheme.primary,
                    style = MnTheme.typography.header4
                )
            }
            MnIconButton(onClick = onClick, iconResourceId = R.drawable.ic_null)
        }
    }
}

@Composable
fun NotificationBox(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small)
            )
    ) {
        Column {
            FocusNotificationBox()
            InterruptNotificationBox()
        }
    }
}

@Composable
fun FocusNotificationBox(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "집중시간 알림받기",
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            Text(
                text = "집중・휴식시간이 되면 고양이가 알려줘요",
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular
            )
        }
        MnToggleButton(isChecked = true, onCheckedChange = { value -> })
    }
}

@Composable
fun InterruptNotificationBox(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "딴 짓 방해하기",
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            Text(
                text = "다른 앱을 사용하면 고양이가 방해해요",
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular
            )
        }
        MnToggleButton(isChecked = true, onCheckedChange = { value -> })
    }
}

@Composable
fun SuggestionBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small)
            )
            .padding(MnSpacing.xLarge)
            .noRippleClickable {
                onClick()
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "의견보내기",
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            MnIconButton(onClick = onClick, iconResourceId = R.drawable.ic_null)
        }
    }
}

@DevicePreviews
@Composable
fun MyPageScreenPreview() {
    MnTheme {
        MyPageScreen(isOffline = false)
    }
}

@DevicePreviews
@Composable
fun MyPageScreenOfflinePreview() {
    MnTheme {
        MyPageScreen(isOffline = true)
    }
}
