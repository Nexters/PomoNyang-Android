package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import android.os.Build
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
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
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.screen.common.LoadingContentContainer
import com.pomonyang.mohanyang.presentation.screen.common.NetworkErrorScreen
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun OnboardingSelectCatRoute(
    selectedCatNo: Int,
    onBackClick: () -> Unit,
    onStartClick: (Int, String, String) -> Unit,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
    onboardingSelectCatViewModel: OnboardingSelectCatViewModel = hiltViewModel(),
) {
    val state by onboardingSelectCatViewModel.state.collectAsStateWithLifecycle()

    onboardingSelectCatViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is SelectCatSideEffect.OnNavToNaming -> {
                onStartClick(
                    effect.no,
                    effect.catName,
                    effect.catTypeName,
                )
            }

            is SelectCatSideEffect.GoToBack -> {
                onBackClick()
            }

            is SelectCatSideEffect.ShowSnackBar -> {
                onShowSnackBar(effect.message)
            }
        }
    }

    NotificationPermissionEffect {
        onboardingSelectCatViewModel.handleEvent(SelectCatEvent.OnGrantedAlarmPermission)
    }

    LaunchedEffect(Unit) {
        onboardingSelectCatViewModel.handleEvent(SelectCatEvent.Init(selectedCatNo))
    }

    LoadingContentContainer(isLoading = state.isLoading) {
        when {
            state.isInvalidError -> NetworkErrorScreen(onClickRetry = { onboardingSelectCatViewModel.handleEvent(SelectCatEvent.OnClickRetry) })

            else -> {
                OnboardingSelectCatScreen(
                    onBackClick = onBackClick,
                    modifier = modifier,
                    onAction = onboardingSelectCatViewModel::handleEvent,
                    state = state,
                )
            }
        }
    }
}

@Composable
private fun OnboardingSelectCatScreen(
    onBackClick: () -> Unit,
    onAction: (SelectCatEvent) -> Unit,
    state: SelectCatState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary),
    ) {
        MnTopAppBar(
            topAppBarColors = MnAppBarColors(
                containerColor = MnTheme.backgroundColorScheme.primary,
                navigationIconContentColor = MnTheme.iconColorScheme.primary,
                titleContentColor = MnTheme.textColorScheme.primary,
                actionIconContentColor = MnTheme.iconColorScheme.primary,
            ),
            navigationIcon = {
                MnIconButton(onClick = onBackClick, iconResourceId = R.drawable.ic_chevron_left)
            },
            content = {
                Text(
                    stringResource(R.string.onboarding_select),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = MnSpacing.large)
                .padding(top = MnSpacing.medium, bottom = MnSpacing.small),
        ) {
            Text(
                stringResource(R.string.onboarding_select_title),
                style = MnTheme.typography.header3,
                color = MnTheme.textColorScheme.primary,
            )
            Text(
                stringResource(R.string.onboarding_select_subtitle),
                modifier = Modifier.padding(bottom = 42.dp),
                style = MnTheme.typography.bodyRegular,
                color = MnTheme.textColorScheme.secondary,
            )
            AlarmExample(
                title = stringResource(R.string.onboarding_select_alarm_title),
                selectedCat = state.selectedType,
            )

            CatRive(
                modifier = Modifier
                    .padding(
                        top = MnSpacing.medium,
                        bottom = 42.dp,
                    )
                    .fillMaxWidth(),
                riveResource = R.raw.cat_select_2,
                stateMachineName = "State Machine_selectCat",
                fireState = state.selectedType?.catFireInput,
            )
            CatCategory(
                cats = state.cats.toPersistentList(),
                selectedType = state.selectedType,
                onClickCatType = { type ->
                    onAction(SelectCatEvent.OnSelectType(type))
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.onboarding_select_start),
                isEnabled = state.selectedType != null,
                onClick = { onAction(SelectCatEvent.OnStartClick) },
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large,
            )
        }
    }
}

@Composable
private fun AlarmExample(
    title: String,
    selectedCat: CatType?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.xSmall),
            )
            .fillMaxWidth()
            .height(72.dp)
            .padding(14.dp),
    ) {
        if (selectedCat != null) {
            SelectedAlarmExample(
                title = title,
                content = stringResource(selectedCat.backgroundPushContent),
            )
        } else {
            EmptyAlarmExample()
        }
    }
}

@Composable
fun SelectedAlarmExample(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        MnColor.Orange100,
                        shape = RoundedCornerShape(8.5.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(painter = painterResource(id = R.drawable.ic_app), contentDescription = "alarm_img")
            }

            Column {
                Text(
                    title,
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                )
                Text(
                    content,
                    style = MnTheme.typography.bodyRegular,
                    color = MnTheme.textColorScheme.secondary,
                )
            }
        }
        Text(
            stringResource(id = R.string.onboarding_select_alarm_time),
            style = MnTheme.typography.bodyRegular,
            color = MnTheme.textColorScheme.secondary,
        )
    }
}

@Composable
fun EmptyAlarmExample(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.onboarding_select_alarm_empty),
            style = MnTheme.typography.bodyRegular,
            color = MnColor.Gray400,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CatCategory(
    cats: PersistentList<CatInfoModel>,
    selectedType: CatType?,
    onClickCatType: (CatType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        cats.map { cat ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
            ) {
                MnSelectButton(
                    modifier = Modifier.fillMaxSize(),
                    isSelected = selectedType == cat.type,
                    onClick = { onClickCatType(cat.type) },
                    subTitleContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                stringResource(cat.type.personality),
                                color = MnTheme.textColorScheme.tertiary,
                                style = MnTheme.typography.subBodyRegular,
                            )

                            MnXSmallIcon(resourceId = cat.type.personalityIcon, tint = Color.Unspecified)
                        }
                    },
                    titleContent = {
                        Text(cat.type.kor)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionEffect(
    onGranted: () -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onGranted()
        return
    }

    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )

    LaunchedEffect(notificationsPermissionState) {
        val status = notificationsPermissionState.status
        when (status) {
            is PermissionStatus.Granted -> {
                onGranted()
            }

            is PermissionStatus.Denied -> {
                notificationsPermissionState.launchPermissionRequest()
            }
        }
    }

    if (notificationsPermissionState.status is PermissionStatus.Granted) {
        onGranted()
    }
}

@Preview
@Composable
fun PreviewOnboardingSelectScreen() {
    OnboardingSelectCatScreen(
        onBackClick = {},
        state = SelectCatState(
            cats = listOf(
                CatInfoModel(1, "이이오", type = CatType.CHEESE),
                CatInfoModel(1, "까만냥", type = CatType.BLACK),
                CatInfoModel(1, "삼색냥", type = CatType.THREE_COLOR),
            ),
        ),
        onAction = { _ -> },
    )
}
