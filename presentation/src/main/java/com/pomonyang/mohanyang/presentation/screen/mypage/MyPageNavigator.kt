package com.pomonyang.mohanyang.presentation.screen.mypage

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pomonyang.mohanyang.presentation.screen.mypage.profile.CatProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data object MyPage

@Serializable
data object MyPageHome

@Serializable
data object CatProfile

fun NavGraphBuilder.myPageScreen(
    navHostController: NavHostController
) {
    navigation<MyPage>(
        startDestination = MyPageHome
    ) {
        composable<MyPageHome> {
            MyPageRoute(
                onBackClick = { navHostController.popBackStack() },
                onProfileClick = { navHostController.navigate(CatProfile) }
            )
        }

        composable<CatProfile> {
            CatProfileRoute(
                onBackClick = { navHostController.popBackStack() }
            )
        }
    }
}
