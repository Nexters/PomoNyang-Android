package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
fun OnboardingNamingCatRoute(
    catName: String,
    catType: CatType,
    onBackClick: () -> Unit,
    onNavToDestination: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingNamingCatViewModel: OnboardingNamingCatViewModel = hiltViewModel()
) {
    onboardingNamingCatViewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            is NamingSideEffect.NavToNext -> {
                onNavToDestination()
            }
        }
    }

    OnboardingNamingCatScreen(
        catName = catName,
        catType = catType,
        onAction = onboardingNamingCatViewModel::handleEvent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingNamingCatScreen(
    catName: String,
    catType: CatType,
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

    fun isEnabled(): Boolean = name.isNotEmpty() && nameValidationResult.isValid

    LaunchedEffect(name) {
        if (name.isNotEmpty()) {
            nameValidationResult = catNameValidator.verifyName(name)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary)
            .clickableSingle(activeRippleEffect = false) {
                focusManager.clearFocus(true)
                keyboardController?.hide()
            }
            .verticalScroll(rememberScrollState(), reverseScrolling = true),
        verticalArrangement = Arrangement.Bottom


    ) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = onBackClick,
                    iconResourceId = R.drawable.ic_chevron_left
                )
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge)
        ) {
            CatRive(
                modifier = Modifier
                    .padding(top = 130.dp)
                    .fillMaxWidth(),
                isAutoPlay = false,
                stateMachineInput = catType.pomodoroRiveCat,
                stateMachineName = "State Machine_Rename",
                riveResource = R.raw.cat_rename_2,
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
                hint = catName
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge, vertical = MnSpacing.small)
                .imePadding()
        ) {
            MnBoxButton(
                text = stringResource(R.string.complete),
                onClick = { if (isEnabled()) onAction(NamingEvent.OnComplete(name)) },
                isEnabled = isEnabled(),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = MnBoxButtonColorType.primary,
                styles = MnBoxButtonStyles.large
            )
        }


    }
}

@Composable
@Preview(showBackground = true)
fun PreviewOnboardingNamingCatScreen() {
    OnboardingNamingCatScreen(
        catName = "삼색이",
        catType = CatType.CHEESE,
        onAction = { _ -> },
        onBackClick = {}
    )
}
