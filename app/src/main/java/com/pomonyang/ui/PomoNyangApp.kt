package com.pomonyang.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomonyang.navigation.PomoNyangNavHost

@Composable
internal fun PomoNyangApp(
    pomoNyangAppState: PomoNyangAppState,
    modifier: Modifier = Modifier
) {
    PomoNyangApp(
        modifier = modifier,
        pomoNyangAppState = pomoNyangAppState,
        snackbarHostState = remember { SnackbarHostState() }
    )
}

@Composable
private fun PomoNyangApp(
    pomoNyangAppState: PomoNyangAppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        val isOffline by pomoNyangAppState.isOffline.collectAsStateWithLifecycle()

        LaunchedEffect(isOffline) {
            if (isOffline) snackbarHostState.showSnackbar("offline 상태입니다.")
        }

        PomoNyangNavHost(
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = Short
                ) == ActionPerformed
            },
            pomoNyangAppState = pomoNyangAppState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
