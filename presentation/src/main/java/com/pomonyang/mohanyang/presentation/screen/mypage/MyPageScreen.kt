package com.pomonyang.mohanyang.presentation.screen.mypage

import MnToggleButton
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun MyPageRoute(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel = hiltViewModel()
) {
    val state by myPageViewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var isShowDialog by remember {
        mutableStateOf(false)
    }

    myPageViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is MyPageSideEffect.GoToCatProfilePage -> {
                onProfileClick()
            }

            is MyPageSideEffect.CheckNotificationPermission -> {
                if (MnNotificationManager.isNotificationGranted(context)) {
                    effect.onGranted()
                } else {
                    isShowDialog = true
                }
            }

            is MyPageSideEffect.CloseDialog -> {
                isShowDialog = false
            }
        }
    }

    LaunchedEffect(Unit) {
        myPageViewModel.handleEvent(MyPageEvent.Init(MnNotificationManager.isNotificationGranted(context)))
    }

    MyPageScreen(
        state = state,
        onAction = myPageViewModel::handleEvent,
        onBackClick = onBackClick,
        isOffline = false,
        isShowDialog = isShowDialog
    )
}

@Composable
fun MyPageScreen(
    state: MyPageState,
    onAction: (MyPageEvent) -> Unit,
    onBackClick: () -> Unit,
    isOffline: Boolean,
    isShowDialog: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val context = LocalContext.current

    fun openSetting() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
        onAction(MyPageEvent.CloseDialog)
    }

    if (isShowDialog) {
        MnDialog(
            title = "설정에서 알림을 켜주세요",
            subTitle = "안드로이드 설정에서 모하냥 앱의 알림 표시를 허용하면 Push 알림을 받을 수 있어요. 지금 설정하시겠어요?",
            positiveButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "다음에",
                    onClick = { onAction(MyPageEvent.CloseDialog) },
                    colors = MnBoxButtonColorType.tertiary,
                    styles = MnBoxButtonStyles.medium
                )
            },
            negativeButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "설정으로 이동",
                    onClick = { openSetting() },
                    colors = MnBoxButtonColorType.primary,
                    styles = MnBoxButtonStyles.medium
                )
            },
            onDismissRequest = {}
        )
    }

    Column(
        modifier = modifier.background(MnTheme.backgroundColorScheme.primary)
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
                    text = stringResource(id = R.string.my_page_title),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary
                )
            }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(
                horizontal = MnSpacing.xLarge,
                vertical = MnSpacing.medium
            ),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.medium)
        ) {
            item {
                ProfileBox(
                    catName = state.catName,
                    onClick = { onAction(MyPageEvent.ClickCatProfile) }
                )
            }

            item {
                NotificationBox(
                    state = state,
                    onAction = onAction
                )
            }
            item { SuggestionBox() }
        }
    }
}

@Composable
fun ProfileBox(
    catName: String,
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
                    text = stringResource(id = R.string.my_page_cat_profile_title),
                    color = MnTheme.textColorScheme.tertiary,
                    style = MnTheme.typography.subBodyRegular
                )
                Text(
                    text = catName,
                    color = MnTheme.textColorScheme.primary,
                    style = MnTheme.typography.header4
                )
            }
            MnIconButton(onClick = onClick, iconResourceId = R.drawable.ic_chevron_right)
        }
    }
}

@Composable
fun NotificationBox(
    modifier: Modifier = Modifier,
    state: MyPageState,
    onAction: (MyPageEvent) -> Unit
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
            FocusNotificationBox(
                isChecked = state.isTimerNotificationEnabled,
                onChangeValue = { isEnabled ->
                    onAction(MyPageEvent.ChangeTimerNotification(isEnabled))
                }
            )
            InterruptNotificationBox(
                isChecked = state.isInterruptNotificationEnabled,
                onChangeValue = { isEnabled ->
                    onAction(MyPageEvent.ChangeInterruptNotification(isEnabled))
                }
            )
        }
    }
}

@Composable
fun FocusNotificationBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChangeValue: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.my_page_timer_push_title),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            Text(
                text = stringResource(id = R.string.my_page_timer_push_content),
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular
            )
        }
        MnToggleButton(isChecked = isChecked, onCheckedChange = onChangeValue)
    }
}

@Composable
fun InterruptNotificationBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChangeValue: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.my_page_interrupt_push_title),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            Text(
                text = stringResource(id = R.string.my_page_interrupt_push_content),
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular
            )
        }
        MnToggleButton(isChecked = isChecked, onCheckedChange = onChangeValue)
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
                text = stringResource(id = R.string.my_page_suggestion),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4
            )
            MnIconButton(onClick = onClick, iconResourceId = R.drawable.ic_chevron_right)
        }
    }
}

@DevicePreviews
@Composable
fun MyPageScreenPreview() {
    MnTheme {
        MyPageScreen(
            state = MyPageState(),
            onAction = {},
            onBackClick = {},
            isOffline = false,
            isShowDialog = false
        )
    }
}

@DevicePreviews
@Composable
fun MyPageScreenOfflinePreview() {
    MnTheme {
        MyPageScreen(
            state = MyPageState(),
            onAction = {},
            onBackClick = {},
            isOffline = true,
            isShowDialog = false
        )
    }
}
