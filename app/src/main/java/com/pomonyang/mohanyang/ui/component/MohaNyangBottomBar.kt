package com.pomonyang.mohanyang.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.screen.home.Home
import com.pomonyang.mohanyang.presentation.screen.mypage.MyPage
import com.pomonyang.mohanyang.presentation.screen.statistics.Statistics
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import com.pomonyang.mohanyang.ui.BottomNavItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun MohaNyangBottomBar(
    navController: NavHostController,
    items: PersistentList<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MnSpacing.xLarge)
            .navigationBarsPadding(),
    ) {
        items.forEachIndexed { index, bottomNavItem ->
            Column(
                verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = MnSpacing.small)
                    .noRippleClickable {
                        onItemSelected(index)
                        navController.navigate(bottomNavItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
            ) {
                val iconRes = if (selectedIndex == index) {
                    bottomNavItem.selectedIconRes
                } else {
                    bottomNavItem.iconRes
                }
                MnMediumIcon(resourceId = iconRes)
                Text(
                    text = bottomNavItem.label,
                    style = MnTheme.typography.captionRegular,
                    color = MnTheme.textColorScheme.primary,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MohaNyangBottomBarPreview() {
    MnTheme {
        MohaNyangBottomBar(
            navController = NavHostController(LocalContext.current),
            items = persistentListOf(
                BottomNavItem(
                    route = Home,
                    iconRes = R.drawable.ic_house,
                    selectedIconRes = R.drawable.ic_house_fill,
                    label = "홈",
                ),
                BottomNavItem(
                    route = Statistics,
                    iconRes = R.drawable.ic_chart_bar,
                    selectedIconRes = R.drawable.ic_chart_bar_fill,
                    label = "통계",
                ),
                BottomNavItem(
                    route = MyPage,
                    iconRes = R.drawable.ic_user,
                    selectedIconRes = R.drawable.ic_user_fill,
                    label = "프로필",
                ),
            ),
            selectedIndex = 0,
            onItemSelected = {},
        )
    }
}
