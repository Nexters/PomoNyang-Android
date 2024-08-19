package com.pomonyang.mohanyang.presentation.screen.mypage.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.component.CatRive
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle

@Composable
internal fun CatProfileRoute(
    onBackClick: () -> Unit,
    onCatChangeClick: (Int) -> Unit,
    onCatNameChangeClick: (String, Int, CatType) -> Unit,
    modifier: Modifier = Modifier,
    catProfileViewModel: CatProfileViewModel = hiltViewModel()
) {
    val state by catProfileViewModel.state.collectAsStateWithLifecycle()

    catProfileViewModel.effects.collectWithLifecycle { effect ->
        when (
            effect
        ) {
            is CatProfileSideEffect.GoToCatNaming -> {
                onCatNameChangeClick(effect.catName, effect.catNo, effect.catType)
            }

            is CatProfileSideEffect.GoToCatTypeChange -> {
                onCatChangeClick(effect.catNo)
            }
        }
    }

    CatProfileScreen(
        state = state,
        onBackClick = onBackClick,
        onAction = catProfileViewModel::handleEvent,
        modifier = modifier
    )
}

@Composable
private fun CatProfileScreen(
    state: CatProfileState,
    onBackClick: () -> Unit,
    onAction: (CatProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onAction(CatProfileEvent.Init)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MnTheme.backgroundColorScheme.primary)
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
                    text = "나의 고양이",
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary
                )
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            CatRive(
                modifier = Modifier.fillMaxWidth(),
                tooltipMessage = stringResource(id = R.string.cat_profile_tooltip),
                riveResource = R.raw.cat_select_motion,
                riveAnimationName = state.catType.riveAnimation

            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickableSingle(
                        activeRippleEffect = false
                    ) {
                        onAction(CatProfileEvent.ClickNaming)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall)
            ) {
                Text(
                    text = state.catName,
                    style = MnTheme.typography.header4,
                    color = MnTheme.textColorScheme.tertiary
                )
                MnMediumIcon(resourceId = R.drawable.ic_pen, tint = MnTheme.iconColorScheme.primary)
            }
            Spacer(modifier = Modifier.weight(1f))
            MnBoxButton(
                modifier = Modifier.fillMaxWidth(),
                containerPadding = PaddingValues(bottom = MnSpacing.small),
                text = "고양이 바꾸기",
                onClick = { onAction(CatProfileEvent.ClickChangeType) },
                colors = MnBoxButtonColorType.secondary,
                styles = MnBoxButtonStyles.large
            )
        }
    }
}

@DevicePreviews
@Composable
fun PreviewCatProfileScreen() {
    MnTheme {
        CatProfileScreen(
            state = CatProfileState(),
            onBackClick = {},
            onAction = {}
        )
    }
}
