package com.pomonyang.mohanyang.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomonyang.mohanyang.navigation.MohaNyangNavHost
import com.pomonyang.mohanyang.presentation.designsystem.toast.MnToastSnackbarHost
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.coroutines.launch

@Composable
internal fun MohaNyangApp(
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier
) {
    MohaNyangApp(
        modifier = modifier,
        mohaNyangAppState = mohaNyangAppState,
        snackbarHostState = remember { SnackbarHostState() }
    )
}

@Composable
private fun MohaNyangApp(
    mohaNyangAppState: MohaNyangAppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MnTheme.backgroundColorScheme.primary,
        snackbarHost = { MnToastSnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        val isOffline by mohaNyangAppState.isOffline.collectAsStateWithLifecycle()

        LaunchedEffect(isOffline) {
            if (isOffline) snackbarHostState.showSnackbar("offline 상태입니다.")
        }

        MohaNyangNavHost(
            onShowSnackbar = { message, action ->
                mohaNyangAppState.coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = Short
                    )
                }
            },
            mohaNyangAppState = mohaNyangAppState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
