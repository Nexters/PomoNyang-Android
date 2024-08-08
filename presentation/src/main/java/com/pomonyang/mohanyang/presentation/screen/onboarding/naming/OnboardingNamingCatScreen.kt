package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .noRippleClickable {
                focusManager.clearFocus(true)
                keyboardController?.hide()
            }
    ) {
        MnTopAppBar(navigationIcon = {
            MnIconButton(
                onClick = onBackClick,
                iconResourceId = R.drawable.ic_null
            )
        })

        Column(
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge)
                .padding(bottom = MnSpacing.small)
        ) {
            Text(
                modifier = Modifier.padding(
                    top = MnSpacing.twoXLarge,
                    bottom = MnSpacing.medium
                ),
                text = stringResource(R.string.naming_cat),
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.primary
            )
            MnTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                isError = state.isError && name.isNotEmpty(),
                errorMessage = state.errorMessage,
                backgroundColor = MnColor.White,
                onValueChange = { value -> name = value }
            )
            Spacer(modifier = Modifier.weight(1f))
            MnBoxButton(
                text = stringResource(R.string.complete),
                onClick = { onAction(NamingEvent.OnComplete(name)) },
                isEnabled = name.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large
            )
        }
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
