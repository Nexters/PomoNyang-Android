package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.domain.PomodoroCategoryModel
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
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.tooltip
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun PomodoroRoute(
    isNewUser: Boolean,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    pomodoroViewModel: PomodoroViewModel = hiltViewModel()
) {
    val state by pomodoroViewModel.state.collectAsStateWithLifecycle()

    HandleEffect(pomodoroViewModel.effects)

    if (state.showCategoryBottomSheet) {
        PomodoroCategoryBottomSheet(
            onAction = pomodoroViewModel::handleEvent,
            categoryList = state.categoryList,
            selectedCategoryNo = state.selectedCategoryNo
        )
    }

    PomodoroSettingScreen(
        onAction = pomodoroViewModel::handleEvent,
        state = state,
        modifier = modifier,
        isNewUser = isNewUser
    )
}

@Composable
private fun HandleEffect(effects: Flow<PomodoroSideEffect>) {
    effects.collectWithLifecycle {
        Timber.tag("koni").d("handleEffect > $it")
        when (it) {
            is PomodoroSideEffect.ShowSnackBar -> {
                // todo handle error
            }
        }
    }
}

@Composable
fun PomodoroSettingScreen(
    onAction: (PomodoroEvent) -> Unit,
    isNewUser: Boolean,
    state: PomodoroState,
    modifier: Modifier = Modifier
) {
    val pomodoroSelectedCategoryModel by remember(state.selectedCategoryNo) {
        mutableStateOf(state.categoryList.find { it.no == state.selectedCategoryNo })
    }

    LaunchedEffect(Unit) {
        onAction(PomodoroEvent.Init)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MnTheme.backgroundColorScheme.primary,
        contentWindowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
        topBar = {
            MnTopAppBar(
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .noRippleClickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        MnMediumIcon(
                            resourceId = R.drawable.ic_null,
                            tint = MnTheme.iconColorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 119.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            CatRive()
            PomodoroDetailSetting(
                onAction = onAction,
                isNewUser = isNewUser,
                selectedCategoryData = pomodoroSelectedCategoryModel
            )
            StartButton()
        }
    }
}

@Composable
private fun PomodoroCategoryBottomSheet(
    onAction: (PomodoroEvent) -> Unit,
    categoryList: List<PomodoroCategoryModel>,
    selectedCategoryNo: Int,
    modifier: Modifier = Modifier
) {
    MnBottomSheet(
        onDismissRequest = { onAction(PomodoroEvent.DismissCategoryDialog) },
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.change_category_title)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            categoryList.forEach { pomodoroCategoryModel ->
                MnSelectListItem(
                    containerPadding = PaddingValues(bottom = MnSpacing.small),
                    iconResource = R.drawable.ic_null,
                    categoryType = pomodoroCategoryModel.title,
                    onClick = { onAction(PomodoroEvent.ClickNewCategory(pomodoroCategoryModel.no)) },
                    isSelected = pomodoroCategoryModel.no == selectedCategoryNo,
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
                onClick = { onAction(PomodoroEvent.ClickCategoryConfirmButton) },
                colors = MnBoxButtonColorType.secondary
            )
        }
    }
}

@Composable
private fun CatRive(
    showTooltip: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(240.dp)
            .background(MnTheme.backgroundColorScheme.secondary)
            .tooltip(
                modifier = Modifier.padding(top = 20.dp),
                enabled = showTooltip,
                content = stringResource(R.string.tooltip_rest_content),
                showOverlay = false,
                highlightComponent = false
            )
    ) {
        // TODO
    }
}

@Composable
private fun PomodoroDetailSetting(
    onAction: (PomodoroEvent) -> Unit,
    isNewUser: Boolean,
    selectedCategoryData: PomodoroCategoryModel?,
    modifier: Modifier = Modifier
) {
    var categoryTooltip by remember { mutableStateOf(isNewUser) }
    var timeTooltip by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(vertical = MnSpacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CategoryBox(
            categoryName = selectedCategoryData?.title ?: "",
            modifier = Modifier
                .padding(bottom = MnSpacing.medium)
                .clip(RoundedCornerShape(MnRadius.xSmall))
                .clickable { onAction(PomodoroEvent.ClickCategory) }
                .tooltip(
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
        )

        Row(
            modifier = Modifier
                .padding(horizontal = MnSpacing.twoXLarge)
                .tooltip(
                    enabled = timeTooltip,
                    content = stringResource(R.string.tooltip_time_content),
                    anchorPadding = PaddingValues(bottom = MnSpacing.medium),
                    ovalShape = MnRadius.xSmall,
                    onDismiss = { timeTooltip = false }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.medium)
        ) {
            TimeComponent(type = stringResource(R.string.focus), time = stringResource(R.string.minute, selectedCategoryData?.focusTime ?: 0))
            TimeDivider()
            TimeComponent(type = stringResource(R.string.rest), time = stringResource(R.string.minute, selectedCategoryData?.restTime ?: 0))
        }
    }
}

@Composable
private fun CategoryBox(
    categoryName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.xSmall)
            )
            .padding(
                horizontal = MnSpacing.medium,
                vertical = MnSpacing.small
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        MnMediumIcon(
            resourceId = R.drawable.ic_null,
            tint = MnTheme.iconColorScheme.primary
        )
        Text(
            text = categoryName,
            style = MnTheme.typography.subBodySemiBold,
            color = MnTheme.textColorScheme.tertiary
        )
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
private fun StartButton() {
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(color = MnTheme.backgroundColorScheme.accent1)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        MnLargeIcon(
            resourceId = R.drawable.ic_null,
            tint = MnTheme.iconColorScheme.inverse
        )
    }
}

@Composable
@Preview
fun PomodoroStarterScreenPreview() {
    PomodoroSettingScreen(
        isNewUser = false,
        state = PomodoroState(
            categoryList = listOf(
                PomodoroCategoryModel(
                    no = 0,
                    title = "집중",
                    focusTime = 25,
                    restTime = 10
                )
            )
        ),
        onAction = {}
    )
}

@Composable
@Preview
fun CatImagePreview() {
    CatRive()
}

@Composable
@Preview(showBackground = true)
fun FocusBoxPreview() {
    PomodoroDetailSetting(
        onAction = {},
        isNewUser = false,
        selectedCategoryData = PomodoroCategoryModel(
            no = 0,
            title = "집중",
            focusTime = 25,
            restTime = 10
        )
    )
}

@Composable
@Preview(showBackground = true)
fun StartButtonPreview() {
    StartButton()
}
