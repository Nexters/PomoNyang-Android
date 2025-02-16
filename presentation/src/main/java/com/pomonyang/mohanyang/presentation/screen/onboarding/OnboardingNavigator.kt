package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.screen.home.Home
import com.pomonyang.mohanyang.presentation.screen.onboarding.guide.OnboardingGuideRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.naming.OnboardingNamingCatRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.select.OnboardingSelectCatRoute
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

@Serializable
internal data object OnboardingGuide

@Serializable
internal data class OnboardingSelectCat(
    val selectedCatNo: Int,
    val destination: String,
)

@Serializable
internal data class OnboardingNamingCat(
    val catNo: Int,
    val catName: String,
    val destination: String,
    val catTypeName: String,
)

enum class CatSettingDestination {
    POMODORO,
    MY_PAGE,
}

fun NavGraphBuilder.onboardingScreen(
    navHostController: NavHostController,
    onShowSnackbar: (String, Int?) -> Unit,
) {
    navigation<Onboarding>(
        startDestination = OnboardingGuide,
    ) {
        composable<OnboardingGuide> {
            OnboardingGuideRoute(
                onStartClick = {
                    navHostController.navigate(
                        OnboardingSelectCat(
                            selectedCatNo = -1,
                            destination = CatSettingDestination.POMODORO.name,
                        ),
                    )
                },
            )
        }

        composable<OnboardingSelectCat> { navBackStackEntry ->

            val routeData = navBackStackEntry.toRoute<OnboardingSelectCat>()
            val destination = CatSettingDestination.valueOf(routeData.destination)

            OnboardingSelectCatRoute(
                selectedCatNo = routeData.selectedCatNo,
                onBackClick = { navHostController.popBackStack() },
                onStartClick = { catNo, catName, catTypeName ->
                    when (destination) {
                        CatSettingDestination.POMODORO -> {
                            navHostController.navigate(OnboardingNamingCat(catNo = catNo, catName = catName, destination = destination.name, catTypeName = catTypeName))
                        }

                        CatSettingDestination.MY_PAGE -> {
                            navHostController.popBackStack()
                        }
                    }
                },
                onShowSnackBar = {
                    onShowSnackbar(it, null)
                },
            )
        }

        composable<OnboardingNamingCat> { navBackStackEntry ->
            val namingCat = navBackStackEntry.toRoute<OnboardingNamingCat>()
            val catName = namingCat.catName
            val catType = CatType.valueOf(namingCat.catTypeName)
            val destination = CatSettingDestination.valueOf(namingCat.destination)

            OnboardingNamingCatRoute(
                catName = catName,
                catType = catType,
                onNavToHomeClick = {
                    when (destination) {
                        // 온보딩에서 에러가 발생하여 홈으로 돌아가는 경우 가이드로 navigate
                        CatSettingDestination.POMODORO -> {
                            navHostController.navigate(
                                route = OnboardingGuide,
                                navOptions = NavOptions.Builder().setPopUpTo<OnboardingGuide>(true).build(),
                            )
                        }
                        // 마이페이지에서 수정시 에러가 발생하여 홈으로 돌아가는 경우
                        CatSettingDestination.MY_PAGE -> {
                            navHostController.navigate(Home) {
                                popUpTo(Home)
                                launchSingleTop = true
                            }
                        }
                    }
                },
                onBackClick = { navHostController.popBackStack() },
                onNavToDestination = {
                    when (destination) {
                        CatSettingDestination.POMODORO -> {
                            navHostController.navigate(
                                route = Home,
                                navOptions = NavOptions.Builder().setPopUpTo<OnboardingGuide>(true).build(),
                            )
                        }

                        CatSettingDestination.MY_PAGE -> {
                            navHostController.popBackStack()
                        }
                    }
                },
            )
        }
    }
}
