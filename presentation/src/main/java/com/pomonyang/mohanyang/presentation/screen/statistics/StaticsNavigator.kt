package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object StatisticsGraph

@Serializable
data object Statistics

fun NavGraphBuilder.statisticsScreen(
    onShowSnackbar: (String, Int?) -> Unit,
    navHostController: NavHostController,
) {
    navigation<StatisticsGraph>(
        startDestination = Statistics,
    ) {
        composable<Statistics>(
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
        }
    }
}
