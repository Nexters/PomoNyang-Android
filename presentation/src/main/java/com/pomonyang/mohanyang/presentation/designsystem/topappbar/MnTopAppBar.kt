package com.pomonyang.mohanyang.presentation.designsystem.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import com.pomonyang.mohanyang.presentation.util.dpToPx

@Composable
fun MnTopAppBar(
    modifier: Modifier = Modifier,
    contentStyle: TextStyle = MnTheme.typography.bodySemiBold,
    topAppBarColors: MnAppBarColors = MnTopAppBarDefaults.topAppBarColors(),
    windowInsets: WindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
    content: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    val iconHorizontalPadding = MnTopAppBarDefaults.iconHorizontalPadding.dpToPx().toInt()
    val topAppBarHeightToPx = MnTopAppBarDefaults.height.dpToPx().toInt()
    Layout(
        content = {
            Box(Modifier.layoutId(NAVIGATION_ICON)) {
                CompositionLocalProvider(
                    value = LocalContentColor provides topAppBarColors.navigationIconContentColor,
                    content = navigationIcon,
                )
            }
            Box(Modifier.layoutId(ACTIONS)) {
                CompositionLocalProvider(
                    value = LocalContentColor provides topAppBarColors.actionIconContentColor,
                    content = actions,
                )
            }
            Box(Modifier.layoutId(CONTENT)) {
                CompositionLocalProvider(
                    LocalContentColor provides topAppBarColors.titleContentColor,
                    LocalTextStyle provides contentStyle,
                    content = content,
                )
            }
        },
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .background(topAppBarColors.containerColor),
    ) { measurables, constraints ->
        val navigationIconPlaceable = measurables.first { it.layoutId == NAVIGATION_ICON }.measure(constraints)
        val actionsPlaceable = measurables.first { it.layoutId == ACTIONS }.measure(constraints)
        val contentPlaceable = measurables.first { it.layoutId == CONTENT }.measure(constraints)

        layout(constraints.maxWidth, topAppBarHeightToPx) {
            navigationIconPlaceable.place(iconHorizontalPadding, (topAppBarHeightToPx - navigationIconPlaceable.height) / 2)
            actionsPlaceable.place(constraints.maxWidth - actionsPlaceable.width - iconHorizontalPadding, (topAppBarHeightToPx - actionsPlaceable.height) / 2)
            val contentX = (constraints.maxWidth - contentPlaceable.width) / 2
            contentPlaceable.place(contentX, (topAppBarHeightToPx - contentPlaceable.height) / 2)
        }
    }
}

private const val NAVIGATION_ICON = "navigationIcon"
private const val ACTIONS = "actions"
private const val CONTENT = "content"

@Preview
@Composable
private fun MnTopAppBarPreview() {
    MnTheme {
        val topAppBarColors = MnTopAppBarDefaults.topAppBarColors()
        MnTopAppBar(
            content = {
                Text("Title")
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickableSingle(activeRippleEffect = false) { },
                    contentAlignment = Alignment.Center,
                ) {
                    MnMediumIcon(
                        resourceId = R.drawable.ic_null,
                        tint = topAppBarColors.navigationIconContentColor,
                    )
                }
            },
            actions = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickableSingle(activeRippleEffect = false) { },
                    contentAlignment = Alignment.Center,
                ) {
                    MnMediumIcon(
                        resourceId = R.drawable.ic_null,
                        tint = topAppBarColors.navigationIconContentColor,
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun MnTopAppBarNotUseActionsPreview() {
    MnTheme {
        val topAppBarColors = MnTopAppBarDefaults.topAppBarColors()
        MnTopAppBar(
            content = {
                Text("Title")
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickableSingle(activeRippleEffect = false) { },
                    contentAlignment = Alignment.Center,
                ) {
                    MnMediumIcon(
                        resourceId = R.drawable.ic_null,
                        tint = topAppBarColors.navigationIconContentColor,
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun MnTopAppBarNotUseNaivigationPreview() {
    MnTheme {
        val topAppBarColors = MnTopAppBarDefaults.topAppBarColors()
        MnTopAppBar(
            content = {
                Text("Title")
            },
            actions = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickableSingle(activeRippleEffect = false) { },
                    contentAlignment = Alignment.Center,
                ) {
                    MnMediumIcon(
                        resourceId = R.drawable.ic_null,
                        tint = topAppBarColors.navigationIconContentColor,
                    )
                }
            },
        )
    }
}
