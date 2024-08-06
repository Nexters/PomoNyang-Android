package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
internal fun OnboardingRoute(
    onNavigateUp: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        onboardingViewModel.handleEvent(OnboardingEvent.Init)
    }

    OnboardingScreen(
        modifier = modifier,
        onHomeClick = onHomeClick
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit
) {
    var isShowing by remember { mutableStateOf(false) }

    if (isShowing) {
        MnDialog(
            title = "Dialog Title",
            subTitle = "Home으로 이동하시겠어요?",
            onDismissRequest = { isShowing = false },
            positiveButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {
                        isShowing = false
                        onHomeClick()
                    }
                ) {
                    Text("확인")
                }
            },
            negativeButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {
                        isShowing = false
                        onHomeClick()
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button({ isShowing = true }) {
            Text(text = "go to home")
        }
    }
}

@ThemePreviews
@DevicePreviews
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen {}
}
