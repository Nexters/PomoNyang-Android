package com.pomonyang.mohanyang.presentation.screen.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews

@Composable
fun FocusStatisticBox(
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 375.dp)
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.medium)
            )
            .padding(MnSpacing.xLarge)
    ) {
        if (isOffline) {
            OfflineAlertBox(modifier = modifier.matchParentSize())
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MnSpacing.medium)
            ) {
                Row {
                    FocusTotalTime(modifier = Modifier.weight(1f), title = "오늘 집중시간", time = "3시간 35분")
                    FocusTotalTime(modifier = Modifier.weight(1f), title = "이번주 집중시간", time = "3시간 35분")
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = MnColor.White,
                            shape = RoundedCornerShape(MnRadius.xSmall)
                        )
                        .padding(horizontal = MnSpacing.xSmall, vertical = MnSpacing.small)

                ) {
                    // TODO API 연결
                    LazyColumn {
                        itemsIndexed(PomodoroCategoryType.values()) { idx, category ->
                            FocusCategoryDataBox(categoryModel = PomodoroCategoryModel(categoryNo = 1, title = stringResource(id = category.kor), categoryType = category, focusTime = 10, restTime = 20))
                        }
                    }
                }

                Text(text = "누적집중시간 3일 21시간 19분", style = MnTheme.typography.subBodySemiBold, color = MnTheme.textColorScheme.tertiary)
            }
        }
    }
}

@Composable
fun OfflineAlertBox(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(110.dp),
                painter = painterResource(id = R.drawable.ic_null),
                contentDescription = "offline_focus"
            )
            Text(
                modifier = Modifier.padding(top = MnSpacing.medium),
                text = "지금은 통계를 확인할 수 없어요",
                style = MnTheme.typography.bodySemiBold,
                color = MnTheme.textColorScheme.primary
            )
            Text(
                text = "인터넷에 연결하면 통계를 볼 수 있어요",
                style = MnTheme.typography.subBodyRegular,
                color = MnTheme.textColorScheme.secondary
            )
        }
    }
}

@Composable
fun FocusTotalTime(
    title: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall)
    ) {
        Text(
            title,
            style = MnTheme.typography.subBodyRegular,
            color = MnTheme.textColorScheme.secondary
        )
        Text(
            time,
            style = MnTheme.typography.header4,
            color = MnTheme.textColorScheme.primary
        )
    }
}

@Composable
fun FocusCategoryDataBox(
    categoryModel: PomodoroCategoryModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MnLargeIcon(resourceId = categoryModel.categoryType.iconRes, tint = Color.Unspecified)
        Text(
            modifier = Modifier.weight(1f),
            text = categoryModel.title,
            style = MnTheme.typography.bodySemiBold,
            color = MnTheme.textColorScheme.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall)
        ) {
            Text(
                text = "집중 ${categoryModel.focusTime}분",
                style = MnTheme.typography.subBodyRegular,
                color = MnTheme.textColorScheme.tertiary
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(
                        12.dp
                    )
                    .clip(CircleShape)
                    .background(MnTheme.textColorScheme.tertiary)
            )
            Text(
                text = "집중 ${categoryModel.focusTime}분",
                style = MnTheme.typography.subBodyRegular,
                color = MnTheme.textColorScheme.tertiary
            )
        }
    }
}

@DevicePreviews
@Composable
fun PreviewFocusStaticsBox() {
    MnTheme {
        FocusStatisticBox(isOffline = false)
    }
}

@DevicePreviews
@Composable
fun PreviewOfflineFocusStaticsBox() {
    MnTheme {
        FocusStatisticBox(isOffline = true)
    }
}
