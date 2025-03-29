package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.bottomsheet.MnBottomSheet
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnIconSize
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CategoryIconBottomSheet(
    onAction: (CategorySettingEvent) -> Unit,
    icons: ImmutableList<CategoryIcon>,
    selectedIcon: CategoryIcon?,
    modifier: Modifier = Modifier,
) {
    MnBottomSheet(
        onDismissRequest = {
            onAction.invoke(CategorySettingEvent.DismissBottomSheet)
        },
        modifier = modifier,
    ) {
        CategoryIconBottomSheetContent(
            onAction = onAction,
            icons = icons,
            selectedIcon = selectedIcon,
        )
    }
}

@Composable
private fun CategoryIconBottomSheetContent(
    onAction: (CategorySettingEvent) -> Unit,
    icons: ImmutableList<CategoryIcon>,
    selectedIcon: CategoryIcon?,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(MnSpacing.medium),
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
        contentPadding = PaddingValues(MnSpacing.xLarge),
    ) {
        items(icons) { icon ->
            CategoryIconKey(
                iconId = icon.resourceId,
                isSelected = selectedIcon == icon,
                onClick = { onAction.invoke(CategorySettingEvent.SelectIcon(icon)) },
            )
        }
    }
}

@Composable
private fun CategoryIconKey(
    @DrawableRes iconId: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) MnTheme.backgroundColorScheme.accent1 else Color.Transparent

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(
                    BorderStroke(1.dp, color = borderColor),
                    shape = RoundedCornerShape(MnRadius.xSmall),
                )
                .noRippleClickable { onClick(iconId) },
            contentAlignment = Alignment.Center,
        ) {
            MnLargeIcon(
                modifier = Modifier.size(MnIconSize.large),
                resourceId = iconId,
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCategoryIconKey() {
    MnTheme {
        Row {
            CategoryIconKey(
                iconId = R.drawable.ic_focus,
                isSelected = true,
                onClick = { },
            )
            CategoryIconKey(
                iconId = R.drawable.ic_category_default,
                isSelected = false,
                onClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCategoryBottomSheetContent() {
    MnTheme {
        CategoryIconBottomSheetContent(
            onAction = {},
            icons = CategoryIcon.entries.toImmutableList(),
            selectedIcon = CategoryIcon.CAT,
        )
    }
}

@Preview
@Composable
private fun PreviewCategoryBottomSheet() {
    MnTheme {
        CategoryIconBottomSheet(
            onAction = {},
            icons = CategoryIcon.entries.toImmutableList(),
            selectedIcon = CategoryIcon.FOCUS,
        )
    }
}
