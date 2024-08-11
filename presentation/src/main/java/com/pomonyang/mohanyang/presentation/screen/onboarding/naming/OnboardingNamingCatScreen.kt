package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.textfield.MnTextField
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.tooltip.tooltip
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun OnboardingNamingCatRoute(
    onBackClick: () -> Unit,
    onNavToHome: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingNamingCatViewModel: OnboardingNamingCatViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by onboardingNamingCatViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            onboardingNamingCatViewModel.effects.collect { effect ->
                when (effect) {
                    is NamingSideEffect.NavToHome -> {
                        onNavToHome()
                    }
                }
            }
        }
    }

    OnboardingNamingCatScreen(
        onAction = onboardingNamingCatViewModel::handleEvent,
        onBackClick = onBackClick,
        state = state,
        modifier = modifier
    )
}

@Composable
fun OnboardingNamingCatScreen(
    onAction: (NamingEvent) -> Unit,
    onBackClick: () -> Unit,
    state: NamingState,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .noRippleClickable {
                focusManager.clearFocus(true)
                keyboardController?.hide()
            }
            .imePadding()

    ) {
        MnTopAppBar(navigationIcon = {
            MnIconButton(
                onClick = onBackClick,
                iconResourceId = R.drawable.ic_null
            )
        })

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(
                    horizontal = MnSpacing.xLarge
                )
        ) {
            item {
                CatRive(modifier = Modifier.padding(top = 130.dp))
                Text(
                    modifier = Modifier.padding(
                        top = MnSpacing.twoXLarge,
                        bottom = MnSpacing.small
                    ),
                    text = stringResource(R.string.naming_cat),
                    style = MnTheme.typography.subBodyRegular,
                    color = MnTheme.textColorScheme.secondary
                )
                MnTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    isError = state.isError && name.isNotEmpty(),
                    errorMessage = state.errorMessage,
                    backgroundColor = MnColor.White,
                    onValueChange = { value -> name = value }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        MnBoxButton(
            text = stringResource(R.string.complete),
            onClick = { onAction(NamingEvent.OnComplete(name)) },
            containerPadding = PaddingValues(
                start = MnSpacing.xLarge,
                end = MnSpacing.xLarge,
                bottom = MnSpacing.small
            ),
            isEnabled = name.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = MnBoxButtonColorType.primary,
            styles = MnBoxButtonStyles.large
        )
    }
}

@Composable
fun CatRive(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(MnTheme.backgroundColorScheme.secondary)
            .tooltip(
                stringResource(id = R.string.naming_cat_tooltip),
                showOverlay = false,
                enabled = false // TODO 툴팁 적용시 포커스 불가문제 해결

            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "image")
    }
}

@Composable
@Preview
fun PreviewOnboardingNamingCatScreen() {
    OnboardingNamingCatScreen(
        onBackClick = {},
        onAction = { _ -> },
        state = NamingState()
    )
}
