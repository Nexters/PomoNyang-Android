package com.pomonyang.mohanyang.presentation.screen.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon.CategoryIconNavType
import com.pomonyang.mohanyang.presentation.screen.home.category.CategorySettingRoute
import com.pomonyang.mohanyang.presentation.screen.home.setting.PomodoroSettingRoute
import com.pomonyang.mohanyang.presentation.screen.home.time.PomodoroTimeSettingRoute
import com.pomonyang.mohanyang.presentation.screen.mypage.MyPage
import com.pomonyang.mohanyang.presentation.screen.pomodoro.Pomodoro
import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data object Home

@Serializable
internal data object PomodoroSetting

@Serializable
internal data class TimeSetting(
    val isFocusTime: Boolean,
    val initialTime: Int,
    val categoryName: String,
)

@Serializable
internal data class CategorySetting(
    val categoryNo: Int?,
    val categoryName: String = "",
    val categoryIcon: CategoryIcon = CategoryIcon.CAT,
)

fun NavGraphBuilder.homeScreen(
    isNewUser: Boolean,
    onShowSnackbar: (String, Int?) -> Unit,
    navHostController: NavHostController,
) {
    navigation<Home>(
        startDestination = PomodoroSetting,
    ) {
        val slideDuration = 500

        composable<PomodoroSetting>(
            popEnterTransition = {
                EnterTransition.None
            },
        ) {
            PomodoroSettingRoute(
                isNewUser = isNewUser,
                onShowSnackbar = onShowSnackbar,
                goTimeSetting = { isFocusTime, initialTime, categoryName ->
                    navHostController.navigate(
                        TimeSetting(isFocusTime, initialTime, categoryName),
                    )
                },
                goToPomodoro = {
                    navHostController.navigate(Pomodoro)
                },
                goToMyPage = {
                    navHostController.navigate(MyPage)
                },
                goToCategoryEdit = { category ->
                    Timber.tag("koni").d("category > $category")
                    navHostController.navigate(
                        CategorySetting(
                            categoryNo = category.categoryNo,
                            categoryName = category.title,
                            categoryIcon = category.categoryIcon,
                        ),
                    )
                },
                goToCategoryCreate = {
                    navHostController.navigate(CategorySetting(categoryNo = null))
                },
            )
        }

        composable<TimeSetting>(
            enterTransition = {
                slideInVertically(tween(slideDuration), initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(tween(slideDuration), targetOffsetY = { it })
            },

            ) { backStackEntry ->
            val routeData = backStackEntry.toRoute<TimeSetting>()
            PomodoroTimeSettingRoute(
                isFocusTime = routeData.isFocusTime,
                initialSettingTime = routeData.initialTime,
                categoryName = routeData.categoryName,
                onShowSnackbar = onShowSnackbar,
            ) {
                navHostController.popBackStack()
            }
        }

        composable<CategorySetting>(
            popEnterTransition = {
                EnterTransition.None
            },
            typeMap = mapOf(typeOf<CategoryIcon>() to CategoryIconNavType),
        ) { backStackEntry ->
            val routeData = backStackEntry.toRoute<CategorySetting>()
            CategorySettingRoute(categoryNo = routeData.categoryNo) {
                navHostController.popBackStack()
            }
        }
    }
}
