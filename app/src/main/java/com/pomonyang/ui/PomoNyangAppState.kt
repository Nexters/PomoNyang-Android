package com.pomonyang.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pomonyang.data.remote.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPomoNyangAppState(
    isNewUser: Boolean,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
    navHostController: NavHostController = rememberNavController()
) = remember(
    isNewUser,
    networkMonitor,
    coroutineScope,
    navHostController
) {
    PomoNyangAppState(
        isNewUser = isNewUser,
        networkMonitor = networkMonitor,
        coroutineScope = coroutineScope,
        navHostController = navHostController
    )
}

@Stable
class PomoNyangAppState(
    val isNewUser: Boolean,
    val navHostController: NavHostController,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}
