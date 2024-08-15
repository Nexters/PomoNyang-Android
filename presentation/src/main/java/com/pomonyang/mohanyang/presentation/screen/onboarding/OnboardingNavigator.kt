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
internal data object OnboardingSelectCat

@Serializable
internal data class OnboardingNamingCat(
    val catNo: Int,
    val catName: String?
)

fun NavGraphBuilder.onboardingScreen(
    navHostController: NavHostController,
    onBackPressed: () -> Unit
) {
    navigation<Onboarding>(
        startDestination = OnboardingGuide
    ) {
        composable<OnboardingGuide> {
            OnboardingGuideRoute(
                onStartClick = {
                    navHostController.navigate(OnboardingSelectCat)
                }
            )
        }

        composable<OnboardingSelectCat> {
            OnboardingSelectCatRoute(
                onBackClick = { navHostController.popBackStack() },
                onNavToNaming = { catNo, catName ->
                    navHostController.navigate(OnboardingNamingCat(catNo = catNo, catName = catName))
                }
            )
        }

        composable<OnboardingNamingCat> { navBackStackEntry ->
            val namingCat = navBackStackEntry.toRoute<OnboardingNamingCat>()
            val catNo = namingCat.catNo
            val catName = namingCat.catName

            OnboardingNamingCatRoute(
                catNo = catNo,
                catName = catName,
                onBackPressed = onBackPressed,
                onBackClick = { navHostController.popBackStack() },
                onNavToHome = {
                    navHostController.navigate(
                        route = Pomodoro,
                        navOptions = NavOptions.Builder().setPopUpTo<OnboardingGuide>(false).build()
                    )
                }
            )
        }
    }
}
