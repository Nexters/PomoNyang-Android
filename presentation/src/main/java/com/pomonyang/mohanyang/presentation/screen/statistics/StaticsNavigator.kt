package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.pomonyang.mohanyang.presentation.util.composableWithDefaultTransition
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
        composableWithDefaultTransition<Statistics> {
            StatisticsRoute(
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}
