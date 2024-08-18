package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.guide.OnboardingGuideRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.naming.OnboardingNamingCatRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.select.OnboardingSelectCatRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.Pomodoro
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

@Serializable
internal data object OnboardingGuide

@Serializable
internal data class OnboardingSelectCat(
    val selectedCatNo: Int,
    val destination: String
)

@Serializable
internal data class OnboardingNamingCat(
    val catNo: Int,
    val catName: String,
    val destination: String,
    val selectedCatRiveAnimation: String
)

enum class CatSettingDestination {
    POMODORO,
    MY_PAGE
}

fun NavGraphBuilder.onboardingScreen(
    navHostController: NavHostController
) {
    navigation<Onboarding>(
        startDestination = OnboardingGuide
    ) {
        composable<OnboardingGuide> {
            OnboardingGuideRoute(
                onStartClick = {
                    navHostController.navigate(
                        OnboardingSelectCat(
                            selectedCatNo = -1,
                            destination = CatSettingDestination.POMODORO.name
                        )
                    )
                }
            )
        }

        composable<OnboardingSelectCat> { navBackStackEntry ->

            val routeData = navBackStackEntry.toRoute<OnboardingSelectCat>()
            val destination = CatSettingDestination.valueOf(routeData.destination)

            OnboardingSelectCatRoute(
                selectedCatNo = routeData.selectedCatNo,
                onBackClick = { navHostController.popBackStack() },
                onStartClick = { catNo, catName, animation ->
                    when (destination) {
                        CatSettingDestination.POMODORO -> {
                            navHostController.navigate(OnboardingNamingCat(catNo = catNo, catName = catName, destination = destination.name, selectedCatRiveAnimation = animation ?: ""))
                        }

                        CatSettingDestination.MY_PAGE -> {
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        }

        composable<OnboardingNamingCat> { navBackStackEntry ->
            val namingCat = navBackStackEntry.toRoute<OnboardingNamingCat>()
            val catName = namingCat.catName
            val selectedCatRiveAnimation = namingCat.selectedCatRiveAnimation
            val destination = CatSettingDestination.valueOf(namingCat.destination)

            OnboardingNamingCatRoute(
                catName = catName,
                selectedCatRiveAnimation = selectedCatRiveAnimation,
                onBackClick = { navHostController.popBackStack() },
                onNavToDestination = {
                    when (destination) {
                        CatSettingDestination.POMODORO -> {
                            navHostController.navigate(
                                route = Pomodoro(isNewUser = true),
                                navOptions = NavOptions.Builder().setPopUpTo<OnboardingGuide>(true).build()
                            )
                        }

                        CatSettingDestination.MY_PAGE -> {
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}
