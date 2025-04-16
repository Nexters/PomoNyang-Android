package com.pomonyang.mohanyang.presentation.screen.mypage

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pomonyang.mohanyang.presentation.screen.mypage.profile.CatProfileRoute
import com.pomonyang.mohanyang.presentation.screen.onboarding.CatSettingDestination
import com.pomonyang.mohanyang.presentation.screen.onboarding.OnboardingNamingCat
import com.pomonyang.mohanyang.presentation.screen.onboarding.OnboardingSelectCat
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
data object MyPage

@Serializable
data object MyPageHome

@Serializable
data object CatProfile

fun NavGraphBuilder.myPageScreen(
    navHostController: NavHostController,
    onShowSnackbar: (String, Int?) -> Unit,
    isOfflineState: StateFlow<Boolean>,
) {
    navigation<MyPage>(
        startDestination = MyPageHome,
    ) {
        composable<MyPageHome> {
            MyPageRoute(
                isOfflineState = isOfflineState,
                onShowSnackBar = onShowSnackbar,
                onBackClick = { navHostController.popBackStack() },
                onProfileClick = { navHostController.navigate(CatProfile) },
            )
        }

        composable<CatProfile> {
            CatProfileRoute(
                onBackClick = { navHostController.popBackStack() },
                onCatChangeClick = { catNo ->
                    navHostController.navigate(OnboardingSelectCat(selectedCatNo = catNo, destination = CatSettingDestination.MY_PAGE.name))
                },
                onCatNameChangeClick = { catName, catNo, catType ->
                    navHostController.navigate(
                        OnboardingNamingCat(catName = catName, catNo = catNo, destination = CatSettingDestination.MY_PAGE.name, catTypeName = catType.name),
                    )
                },
            )
        }
    }
}
