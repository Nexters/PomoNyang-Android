package com.pomonyang.mohanyang.presentation.screen.home.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.picker.MnWheelMinutePicker
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants
import com.pomonyang.mohanyang.presentation.screen.common.LoadingContentContainer
import com.pomonyang.mohanyang.presentation.screen.common.NetworkErrorScreen
import com.pomonyang.mohanyang.presentation.screen.common.ServerErrorScreen
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.setting.SettingButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
fun PomodoroTimeSettingRoute(
    isFocusTime: Boolean,
    categoryName: String,
    initialSettingTime: Int,
    modifier: Modifier = Modifier,
    viewModel: PomodoroTimeSettingViewModel = hiltViewModel(),
    onShowSnackbar: (String, Int?) -> Unit,
    onEndSettingClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is PomodoroTimeSettingEffect.GoToPomodoroSettingScreen -> {
                val message = if (isFocusTime) "집중" else "휴식"
                onShowSnackbar("${message}시간을 변경했어요", R.drawable.ic_check)
                onEndSettingClick()
            }

            is PomodoroTimeSettingEffect.ClosePomodoroTimerSettingScreen -> {
                onEndSettingClick()
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.handleEvent(
            PomodoroTimeSettingEvent.Init(isFocusTime = isFocusTime),
        )
    }

    when {
        state.isInternalError -> {
            ServerErrorScreen(onClickNavigateToHome = onEndSettingClick)
        }

        state.isInvalidError -> {
            NetworkErrorScreen(onClickRetry = {
                viewModel.handleEvent(PomodoroTimeSettingEvent.ClickRetry)
            })
        }

        else -> {
            LoadingContentContainer(isLoading = state.isLoading) {
                PomodoroTimeSettingScreen(
                    modifier = modifier,
                    category = state.categoryIcon,
                    isFocusTime = isFocusTime,
                    categoryName = categoryName,
                    initialSettingTime = initialSettingTime,
                    onAction = viewModel::handleEvent,
                )
            }
        }
    }
}

@Composable
private fun PomodoroTimeSettingScreen(
    initialSettingTime: Int,
    isFocusTime: Boolean,
    category: CategoryIcon,
    categoryName: String,
    onAction: (PomodoroTimeSettingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconResId = if (isFocusTime) R.drawable.ic_fire else R.drawable.ic_lightning

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        TimeSettingTopAppBar { onAction(PomodoroTimeSettingEvent.ClickClose) }

        CategoryBox(
            iconRes = category.resourceId,
            categoryName = categoryName,
            backgroundColor = Color.Transparent,
        )

        MnWheelMinutePicker(
            modifier = Modifier.padding(top = 16.dp),
            minTime = if (isFocusTime) PomodoroConstants.MIN_FOCUS_MINUTES else PomodoroConstants.MIN_REST_MINUTES,
            maxTime = if (isFocusTime) PomodoroConstants.MAX_FOCUS_MINUTES else PomodoroConstants.MAX_REST_MINUTES,
            initialItem = initialSettingTime,
            onChangePickTime = remember { { onAction(PomodoroTimeSettingEvent.ChangePickTime(time = it)) } },
            selectedIconResId = iconResId,
        )

        SettingButton(
            iconRes = R.drawable.ic_check_32,
            modifier = Modifier.padding(bottom = 40.dp),
            backgroundColor = MnTheme.backgroundColorScheme.inverse,
            onClick = {
                onAction(PomodoroTimeSettingEvent.Submit)
            },
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimeSettingTopAppBar(
    onActionClick: () -> Unit,
) {
    MnTopAppBar(
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickableSingle(activeRippleEffect = false) { onActionClick() },
                contentAlignment = Alignment.Center,
            ) {
                MnMediumIcon(
                    resourceId = R.drawable.ic_close,
                    tint = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PomodoroTimeSettingScreenPreview() {
    MnTheme {
        PomodoroTimeSettingScreen(
            initialSettingTime = 25,
            isFocusTime = true,
            onAction = {},
            category = CategoryIcon.CAT,
            categoryName = "카테고리",
        )
    }
}
