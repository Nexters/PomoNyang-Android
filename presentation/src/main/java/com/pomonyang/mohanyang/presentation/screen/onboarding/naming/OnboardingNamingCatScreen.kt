package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
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
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingNamingCatScreen(
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingNamingCatScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val name = remember {
        mutableStateOf("")
    }

    val isError = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .background(MnTheme.backgroundColorScheme.primary)
            .fillMaxSize()
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
                modifier = Modifier.padding(top = MnSpacing.twoXLarge),
                text = LocalContext.current.getString(R.string.naming_cat),
                style = MnTheme.typography.header5,
                color = MnTheme.textColorScheme.primary
            )
            MnTextField(
                modifier = Modifier
                    .padding(top = MnSpacing.medium)
                    .fillMaxWidth(),
                value = name.value,
                isError = isError.value,
                errorMessage = "임시로 넣어놓을게요",
                backgroundColor = MnColor.White,
                onValueChange = { value ->
                    name.value = value
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            MnBoxButton(
                text = LocalContext.current.getString(R.string.complete),
                onClick = { isError.value = !isError.value },
                isEnabled = !isError.value,
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
    OnboardingNamingCatScreen(onHomeClick = { }, onBackClick = {})
}
