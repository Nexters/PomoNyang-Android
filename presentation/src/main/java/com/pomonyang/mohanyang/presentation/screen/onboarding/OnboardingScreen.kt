package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
internal fun OnboardingRoute(
    onNavigateUp: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            positiveButtonLabel = "이동",
            negativeButtonLabel = "취소",
            onPositiveButtonClick = {
                isShowing = false
                onHomeClick()
            },
            onNegativeButtonClick = { isShowing = false },
            onDismissRequest = { isShowing = false }
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
