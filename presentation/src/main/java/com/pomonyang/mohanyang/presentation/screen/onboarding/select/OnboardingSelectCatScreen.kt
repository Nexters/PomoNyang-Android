package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectButton
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnAppBarColors
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.displayAlarm
import java.time.LocalTime

@Composable
fun OnboardingSelectCatRoute(
    onNamingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingSelectCatScreen(
        onNamingClick = onNamingClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingSelectCatScreen(
    onNamingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO 고양이 목록 조회 API 연결
    val cats = listOf("치즈냥", "삼색냥", "까만냥")

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary)
    ) {
        MnTopAppBar(
            topAppBarColors = MnAppBarColors(
                containerColor = MnTheme.backgroundColorScheme.primary,
                navigationIconContentColor = MnTheme.iconColorScheme.primary,
                titleContentColor = MnTheme.textColorScheme.primary,
                actionIconContentColor = MnTheme.iconColorScheme.primary
            ),
            navigationIcon = {
                MnIconButton(onClick = { /*TODO*/ }, iconResourceId = R.drawable.ic_null)
            },
            content = {
                Text(
                    context.getString(R.string.onboarding_select),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = MnSpacing.large)
                .padding(top = MnSpacing.medium, bottom = MnSpacing.small)
        ) {
            Text(
                context.getString(R.string.onboarding_select_title),
                style = MnTheme.typography.header3,
                color = MnTheme.textColorScheme.primary
            )
            Text(
                context.getString(R.string.onboarding_select_subtitle),
                modifier = Modifier.padding(bottom = 47.dp),
                style = MnTheme.typography.bodyRegular,
                color = MnTheme.textColorScheme.secondary
            )
            AlarmExample(
                title = context.getString(R.string.onboarding_select_alarm_title),
                content = context.getString(R.string.onboarding_select_alarm_content)

            )

            CatRive(modifier = Modifier.padding(top = MnSpacing.medium, bottom = 43.dp))

            CatCategory(catCategory = cats)

            Spacer(modifier = Modifier.weight(1f))

            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                text = context.getString(R.string.onboarding_select_start),
                onClick = { /*TODO*/ },
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large
            )
        }
    }
}

@Composable
private fun AlarmExample(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(MnRadius.small)
            )
            .background(MnTheme.backgroundColorScheme.secondary)
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.5.dp))
                        .background(MnTheme.backgroundColorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_null), contentDescription = "alarm_img")
                }
                Column {
                    Text(
                        title,
                        style = MnTheme.typography.bodySemiBold,
                        color = MnTheme.textColorScheme.primary
                    )
                    Text(
                        content,
                        style = MnTheme.typography.bodyRegular,
                        color = MnTheme.textColorScheme.secondary
                    )
                }
            }
            Text(
                LocalTime.now().displayAlarm(),
                style = MnTheme.typography.bodyRegular,
                color = MnTheme.textColorScheme.secondary
            )
        }
    }
}

@Composable
private fun CatRive(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(240.dp)
            .fillMaxWidth()
            .background(MnColor.Gray200),
        contentAlignment = Alignment.Center
    ) {
        Text("image")
    }
}

@Composable
private fun CatCategory(
    catCategory: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        catCategory.map { cat ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            ) {
                MnSelectButton(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { /*TODO*/ },
                    subTitleContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cat)
                            MnXSmallIcon(resourceId = R.drawable.ic_null)
                        }
                    },
                    titleContent = {
                        Text(cat)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAlarmExample() {
    AlarmExample(title = "알람", content = "알람 텍스트")
}

@Preview
@Composable
fun PreviewCatCategory() {
    CatCategory(catCategory = listOf("1", "2", "3"))
}

@Preview
@Composable
fun PreviewOnboardingSelectScreen() {
    OnboardingSelectCatScreen(onNamingClick = { /*TODO*/ })
}
