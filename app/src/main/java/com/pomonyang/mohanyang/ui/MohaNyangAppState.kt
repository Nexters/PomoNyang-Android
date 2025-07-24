package com.pomonyang.mohanyang.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.presentation.screen.home.PomodoroSetting
import com.pomonyang.mohanyang.presentation.screen.mypage.MyPageHome
import com.pomonyang.mohanyang.presentation.screen.statistics.Statistics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberMohaNyangAppState(
    isNewUser: Boolean,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
    navHostController: NavHostController = rememberNavController(),
) = remember(
    isNewUser,
    networkMonitor,
    coroutineScope,
    navHostController,
) {
    MohaNyangAppState(
        isNewUser = isNewUser,
        networkMonitor = networkMonitor,
        coroutineScope = coroutineScope,
        navHostController = navHostController,
    )
}

@Stable
class MohaNyangAppState(
    val isNewUser: Boolean,
    val navHostController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val showBottomBar: Boolean
        @Composable
        get() {
            val route = currentDestination?.route
            val bottomBarRoutes = listOfNotNull(
                Statistics::class,
                MyPageHome::class,
                PomodoroSetting::class,
            ).map { it.qualifiedName }
            return route in bottomBarRoutes
        }

    private val previousDestination = mutableStateOf<NavDestination?>(null)

    private val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navHostController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentBottomRootRoute: String?
        @Composable get() = currentDestination?.parent?.route
}
