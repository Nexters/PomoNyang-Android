package com.pomonyang.mohanyang.presentation.screen.mypage

import MnToggleButton
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MyPageRoute(
    isOfflineState: StateFlow<Boolean>,
    onShowSnackBar: (String, Int?) -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
) {
    val state by myPageViewModel.state.collectAsStateWithLifecycle()

    val isOffline by isOfflineState.collectAsStateWithLifecycle()

    val context = LocalContext.current as Activity
    var isShowDialog by remember {
        mutableStateOf(false)
    }

    var permissionRequest by remember {
        mutableStateOf<NotificationRequest?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (MnNotificationManager.isNotificationGranted(context)) {
            permissionRequest?.let {
                when (it) {
                    NotificationRequest.TIMER -> {
                        myPageViewModel.handleEvent(MyPageEvent.ChangeTimerNotification(true))
                    }

                    NotificationRequest.INTERRUPT -> {
                        myPageViewModel.handleEvent(MyPageEvent.ChangeInterruptNotification(true))
                    }

                    NotificationRequest.LOCKSCREEN -> {
                        myPageViewModel.handleEvent(MyPageEvent.ChangeLockScreenNotification(true))
                    }
                }
            }
        }
    }

    fun openSetting() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        launcher.launch(intent)
        myPageViewModel.handleEvent(MyPageEvent.CloseDialog)
    }

    myPageViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is MyPageSideEffect.ShowSnackBar -> {
                onShowSnackBar(effect.message, R.drawable.ic_alert)
            }

            is MyPageSideEffect.GoToCatProfilePage -> {
                onProfileClick()
            }

            is MyPageSideEffect.CheckNotificationPermission -> {
                if (MnNotificationManager.isNotificationGranted(context)) {
                    effect.onGranted()
                } else {
                    isShowDialog = true
                    permissionRequest = effect.request
                }
            }

            is MyPageSideEffect.CloseDialog -> {
                isShowDialog = false
            }

            is MyPageSideEffect.OpenDialog -> {
                openSetting()
            }

            is MyPageSideEffect.OpenExternalWebPage -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(effect.url)))
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
        isOffline = isOffline,
        isShowDialog = isShowDialog,
        modifier = modifier,
    )
}

enum class NotificationRequest { TIMER, INTERRUPT, LOCKSCREEN }

@Composable
fun MyPageScreen(
    state: MyPageState,
    onAction: (MyPageEvent) -> Unit,
    onBackClick: () -> Unit,
    isOffline: Boolean,
    isShowDialog: Boolean,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    if (isShowDialog) {
        MnDialog(
            title = stringResource(id = R.string.notification_dialog_title),
            subTitle = stringResource(id = R.string.notification_dialog_sub_title),
            positiveButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.notification_dialog_cancel),
                    onClick = { onAction(MyPageEvent.CloseDialog) },
                    colors = MnBoxButtonColorType.tertiary,
                    styles = MnBoxButtonStyles.medium,
                )
            },
            negativeButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.notification_dialog_move),
                    onClick = { onAction(MyPageEvent.OpenSetting) },
                    colors = MnBoxButtonColorType.primary,
                    styles = MnBoxButtonStyles.medium,
                )
            },
            onDismissRequest = {
                onAction(MyPageEvent.CloseDialog)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary),
    ) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = onBackClick,
                    iconResourceId = R.drawable.ic_chevron_left,
                )
            },
            content = {
                Text(
                    text = stringResource(id = R.string.my_page_title),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                )
            },
        )

        Column(
            modifier = Modifier
                .padding(
                    horizontal = MnSpacing.xLarge,
                    vertical = MnSpacing.medium,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MnSpacing.medium),
        ) {
            ProfileBox(
                catName = state.catName,
                onClick = {
                    onAction(MyPageEvent.ClickCatProfile(isOffline))
                },
            )
            CheckFocusTimeBox()
            NotificationBox(
                state = state,
                onAction = onAction,
            )
            SuggestionBox {
                onAction(MyPageEvent.ClickSuggestion)
            }
        }
    }
}

@Composable
fun ProfileBox(
    catName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small),
            )
            .padding(MnSpacing.xLarge)
            .clickableSingle(activeRippleEffect = false) { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(id = R.string.my_page_cat_profile_title),
                    color = MnTheme.textColorScheme.tertiary,
                    style = MnTheme.typography.subBodyRegular,
                )
                Text(
                    text = catName,
                    color = MnTheme.textColorScheme.primary,
                    style = MnTheme.typography.header4,
                )
            }
            MnIconButton(onClick = onClick, iconResourceId = R.drawable.ic_chevron_right)
        }
    }
}

@Composable
fun CheckFocusTimeBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.large),
            )
            .padding(MnSpacing.xLarge),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_static_ready),
                contentDescription = "static_ready_image",
                modifier = Modifier.size(
                    96.dp,
                ),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MnSpacing.medium),
                verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.my_page_static_title),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.secondary,
                )
                Text(
                    text = stringResource(id = R.string.my_page_static_subtitle),
                    style = MnTheme.typography.subBodyRegular,
                    color = MnTheme.textColorScheme.secondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun NotificationBox(
    modifier: Modifier = Modifier,
    state: MyPageState,
    onAction: (MyPageEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small),
            ),
    ) {
        Column {
            FocusNotificationBox(
                isChecked = state.isTimerNotificationEnabled,
                onChangeValue = { isEnabled ->
                    onAction(MyPageEvent.ChangeTimerNotification(isEnabled))
                },
            )
            InterruptNotificationBox(
                isChecked = state.isInterruptNotificationEnabled,
                onChangeValue = { isEnabled ->
                    onAction(MyPageEvent.ChangeInterruptNotification(isEnabled))
                },
            )
            LockScreenNotificationBox(
                isChecked = state.isLockScreenNotificationEnabled,
                onChangeValue = { isEnabled ->
                    onAction(MyPageEvent.ChangeLockScreenNotification(isEnabled))
                },
            )
        }
    }
}

@Composable
fun FocusNotificationBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChangeValue: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(id = R.string.my_page_timer_push_title),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4,
            )
            Text(
                text = stringResource(id = R.string.my_page_timer_push_content),
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular,
            )
        }
        MnToggleButton(isChecked = isChecked, onCheckedChange = onChangeValue)
    }
}

@Composable
fun InterruptNotificationBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChangeValue: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(id = R.string.my_page_interrupt_push_title),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4,
            )
            Text(
                text = stringResource(id = R.string.my_page_interrupt_push_content),
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular,
            )
        }
        MnToggleButton(isChecked = isChecked, onCheckedChange = onChangeValue)
    }
}

@Composable
fun LockScreenNotificationBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onChangeValue: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.padding(MnSpacing.xLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(id = R.string.my_page_lockscreen_push_title),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4,
            )
            Text(
                text = stringResource(id = R.string.my_page_lockscreen_push_content),
                color = MnTheme.textColorScheme.tertiary,
                style = MnTheme.typography.subBodyRegular,
            )
        }
        MnToggleButton(isChecked = isChecked, onCheckedChange = onChangeValue)
    }
}

@Composable
fun SuggestionBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MnTheme.backgroundColorScheme.secondary,
                shape = RoundedCornerShape(MnRadius.small),
            )
            .padding(MnSpacing.xLarge)
            .clickableSingle(activeRippleEffect = false) {
                onClick()
            },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.my_page_suggestion),
                color = MnTheme.textColorScheme.primary,
                style = MnTheme.typography.header4,
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
            isShowDialog = false,
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
            isShowDialog = false,
        )
    }
}
