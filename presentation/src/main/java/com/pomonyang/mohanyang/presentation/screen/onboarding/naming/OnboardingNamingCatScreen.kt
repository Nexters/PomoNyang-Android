package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.textfield.MnTextField
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.presentation.util.noRippleClickable

@Composable
fun OnboardingNamingCatRoute(
    catName: String,
    onBackClick: () -> Unit,
    onNavToHome: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingNamingCatViewModel: OnboardingNamingCatViewModel = hiltViewModel()
) {
    onboardingNamingCatViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is NamingSideEffect.NavToHome -> {
                onNavToHome()
            }
        }
    }

    OnboardingNamingCatScreen(
        catName = catName,
        onAction = onboardingNamingCatViewModel::handleEvent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingNamingCatScreen(
    catName: String,
    onAction: (NamingEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    var name by rememberSaveable { mutableStateOf(catName) }
    val catNameValidator = remember { CatNameVerifier() }
    var nameValidationResult by remember {
        mutableStateOf(ValidationResult(true))
    }

    LaunchedEffect(name) {
        if (name.isNotEmpty()) {
            nameValidationResult = catNameValidator.verifyName(name)
        }
    }

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
                iconResourceId = R.drawable.ic_chevron_left
            )
        })

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge)

        ) {
            item {
                CatRive(
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    tooltipMessage = stringResource(id = R.string.naming_cat_tooltip)
                )
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
                    isError = !nameValidationResult.isValid,
                    errorMessage = nameValidationResult.message,
                    backgroundColor = MnColor.White,
                    onValueChange = { value -> name = value },
                    hint = catName ?: ""
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            text = stringResource(R.string.complete),
            containerPadding = PaddingValues(
                bottom = MnSpacing.small,
                start = MnSpacing.xLarge,
                end = MnSpacing.xLarge
            ),
            onClick = { onAction(NamingEvent.OnComplete(name)) },
            isEnabled = name.isNotEmpty() && nameValidationResult.isValid,
            modifier = Modifier.fillMaxWidth(),
            colors = MnBoxButtonColorType.primary,
            styles = MnBoxButtonStyles.large
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewOnboardingNamingCatScreen() {
    OnboardingNamingCatScreen(
        catName = "삼색이",
        onAction = { _ -> },
        onBackClick = {}
    )
}
