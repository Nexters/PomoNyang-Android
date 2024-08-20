@file:OptIn(ExperimentalAssetLoader::class)

package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.component.CategoryBox
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.guideTooltip
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PomodoroSettingRoute(
    isNewUser: Boolean,
    onShowSnackbar: suspend (String, Int?) -> Unit,
    goToPomodoro: () -> Unit,
    goTimeSetting: (isFocusTime: Boolean, initialTime: Int, categoryName: String) -> Unit,
    goToMyPage: () -> Unit,
    modifier: Modifier = Modifier,
    pomodoroSettingViewModel: PomodoroSettingViewModel = hiltViewModel()
) {
    val state by pomodoroSettingViewModel.state.collectAsStateWithLifecycle()
    pomodoroSettingViewModel.effects.collectWithLifecycle { effect ->
        if (isNewUser && state.isEndOnBoardingTooltip.not()) return@collectWithLifecycle
        when (effect) {
            is PomodoroSettingSideEffect.ShowSnackBar -> onShowSnackbar(effect.message, effect.iconRes)
            is PomodoroSettingSideEffect.GoTimeSetting -> goTimeSetting(effect.isFocusTime, effect.initialTime, effect.category)
            PomodoroSettingSideEffect.GoToPomodoro -> goToPomodoro()
            PomodoroSettingSideEffect.GoToMyPage -> goToMyPage()
        }
    }

    LaunchedEffect(Unit) {
        pomodoroSettingViewModel.handleEvent(PomodoroSettingEvent.Init)
    }

    if (state.showCategoryBottomSheet) {
        PomodoroCategoryBottomSheet(
            onAction = pomodoroSettingViewModel::handleEvent,
            categoryList = state.categoryList,
            initialCategoryNo = state.selectedCategoryNo
        )
    }

    PomodoroSettingScreen(
        onAction = pomodoroSettingViewModel::handleEvent,
        state = state,
        modifier = modifier,
        showOnboardingTooltip = isNewUser && state.isEndOnBoardingTooltip.not()
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PomodoroSettingScreen(
    onAction: (PomodoroSettingEvent) -> Unit,
    showOnboardingTooltip: Boolean,
    state: PomodoroSettingState,
    modifier: Modifier = Modifier
) {
    val pomodoroSelectedCategoryModel by remember(state.selectedCategoryNo) {
        mutableStateOf(state.categoryList.find { it.categoryNo == state.selectedCategoryNo })
    }
    var categoryTooltip by remember { mutableStateOf(false) }
    var timeTooltip by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(0.5.seconds)
        categoryTooltip = showOnboardingTooltip
    }

    Scaffold(
        modifier = modifier,
        containerColor = MnTheme.backgroundColorScheme.primary,
        topBar = {
            MnTopAppBar(
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickableSingle(activeRippleEffect = false) { onAction(PomodoroSettingEvent.ClickMenu) },
                        contentAlignment = Alignment.Center
                    ) {
                        MnMediumIcon(
                            resourceId = R.drawable.ic_menu,
                            tint = MnTheme.iconColorScheme.primary
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CatRive(
                tooltipMessage = stringResource(R.string.welcome_cat_tooltip),
                riveResource = R.raw.cat_home,
                stateMachineInput = state.cat.type.pomodoroRiveCat,
                stateMachineName = "State Machine_Home",
                isAutoPlay = false,
                onRiveClick = {
                    it.fireState("State Machine_Home", state.cat.type.catFireInput)
                }
            )

            Text(
                text = state.cat.name,
                style = MnTheme.typography.header4,
                color = MnTheme.textColorScheme.tertiary
            )

            CategoryBox(
                iconRes = pomodoroSelectedCategoryModel?.categoryType?.iconRes ?: R.drawable.ic_null,
                categoryName = pomodoroSelectedCategoryModel?.title ?: "",
                modifier = Modifier
                    .padding(bottom = MnSpacing.medium, top = 40.dp)
                    .clip(RoundedCornerShape(MnRadius.xSmall))
                    .guideTooltip(
                        enabled = categoryTooltip,
                        content = stringResource(R.string.tooltip_category_content),
                        anchorPadding = PaddingValues(bottom = MnSpacing.medium),
                        ovalShape = MnRadius.xSmall,
                        onDismiss = {
                            coroutineScope.launch {
                                categoryTooltip = false
                                delay(250)
                                timeTooltip = true
                            }
                        }
                    )
                    .clickableSingle { onAction(PomodoroSettingEvent.ClickCategory) }
            )

            TimeSetting(
                timeTooltip = timeTooltip,
                onAction = onAction,
                onDismiss = { timeTooltip = false },
                pomodoroSelectedCategoryModel = pomodoroSelectedCategoryModel,
                modifier = modifier
            )

            SettingButton(
                iconRes = R.drawable.ic_play_32,
                onClick = { onAction(PomodoroSettingEvent.ClickStartPomodoroSetting) },
                backgroundColor = MnTheme.backgroundColorScheme.accent1
            )
        }
    }
}

@Composable
private fun TimeSetting(
    timeTooltip: Boolean,
    onAction: (PomodoroSettingEvent) -> Unit,
    onDismiss: () -> Unit,
    pomodoroSelectedCategoryModel: PomodoroCategoryModel?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = MnSpacing.twoXLarge)
            .padding(bottom = 40.dp)
            .guideTooltip(
                enabled = timeTooltip,
                content = stringResource(R.string.tooltip_time_content),
                anchorPadding = PaddingValues(bottom = MnSpacing.medium),
                ovalShape = MnRadius.xSmall,
                onDismiss = {
                    onDismiss()
                    onAction(PomodoroSettingEvent.DismissOnBoardingTooltip)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.medium)
    ) {
        TimeComponent(
            modifier = Modifier.clickableSingle(activeRippleEffect = false) { onAction(PomodoroSettingEvent.ClickFocusTime) },
            type = stringResource(R.string.focus),
            time = stringResource(R.string.minute, pomodoroSelectedCategoryModel?.focusTime ?: 0)
        )
        TimeDivider()
        TimeComponent(
            modifier = Modifier.clickableSingle(activeRippleEffect = false) { onAction(PomodoroSettingEvent.ClickRestTime) },
            type = stringResource(R.string.rest),
            time = stringResource(R.string.minute, pomodoroSelectedCategoryModel?.restTime ?: 0)
        )
    }
}

@Composable
private fun PomodoroCategoryBottomSheet(
    onAction: (PomodoroSettingEvent) -> Unit,
    categoryList: List<PomodoroCategoryModel>,
    initialCategoryNo: Int,
    modifier: Modifier = Modifier
) {
    var currentSelectedCategoryNo by remember { mutableIntStateOf(initialCategoryNo) }

    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroSettingEvent.DismissCategoryDialog) },
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.change_category_title)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            categoryList.forEach { pomodoroCategoryModel ->
                MnSelectListItem(
                    containerPadding = PaddingValues(bottom = MnSpacing.small),
                    iconResource = pomodoroCategoryModel.categoryType.iconRes,
                    categoryType = pomodoroCategoryModel.title,
                    onClick = { currentSelectedCategoryNo = pomodoroCategoryModel.categoryNo },
                    isSelected = pomodoroCategoryModel.categoryNo == currentSelectedCategoryNo,
                    restTime = stringResource(R.string.minute, pomodoroCategoryModel.restTime),
                    focusTime = stringResource(R.string.minute, pomodoroCategoryModel.focusTime),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            MnBoxButton(
                containerPadding = PaddingValues(top = MnSpacing.xSmall, bottom = MnSpacing.medium),
                modifier = Modifier.fillMaxWidth(),
                styles = MnBoxButtonStyles.large,
                text = stringResource(R.string.confirm),
                onClick = { onAction(PomodoroSettingEvent.ClickCategoryConfirmButton(currentSelectedCategoryNo)) },
                colors = MnBoxButtonColorType.secondary
            )
        }
    }
}

@Composable
private fun TimeComponent(
    type: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.padding(
            vertical = MnSpacing.xSmall,
            horizontal = MnSpacing.small
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MnColor.Gray500
        ) {
            Text(
                text = type,
                style = MnTheme.typography.bodySemiBold
            )
            Text(
                text = time,
                style = MnTheme.typography.header3
            )
        }
    }
}

@Composable
private fun TimeDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .width(2.dp)
            .height(20.dp)
            .background(color = MnColor.Gray200, shape = RoundedCornerShape(MnRadius.xSmall))
    )
}

@Composable
fun SettingButton(
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickableSingle { onClick() },
        contentAlignment = Alignment.Center
    ) {
        MnLargeIcon(
            resourceId = iconRes,
            tint = MnTheme.iconColorScheme.inverse
        )
    }
}

@Composable
@Preview
fun PomodoroStarterScreenPreview() {
    PomodoroSettingScreen(
        onAction = {},
        showOnboardingTooltip = false,
        state = PomodoroSettingState(
            categoryList = listOf(
                PomodoroCategoryModel(
                    categoryNo = 0,
                    title = "집중",
                    categoryType = PomodoroCategoryType.DEFAULT,
                    focusTime = 25,
                    restTime = 10
                )
            ),
            cat = CatInfoModel(
                no = 0,
                name = "이이오",
                type = CatType.CHEESE
            )
        )
    )
}

@Composable
@Preview(showBackground = true)
fun StartButtonPreview() {
    SettingButton(
        iconRes = 0,
        backgroundColor = MnTheme.backgroundColorScheme.accent1
    ) {}
}
