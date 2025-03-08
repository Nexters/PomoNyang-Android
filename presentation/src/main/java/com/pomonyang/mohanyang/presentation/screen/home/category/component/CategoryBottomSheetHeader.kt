package com.pomonyang.mohanyang.presentation.screen.home.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun CategoryBottomSheetHeader(
    onEditClick: () -> Unit,
    onMoreMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MnIconButton(
            onClick = onEditClick,
            iconResourceId = R.drawable.ic_plus,
        )
        MnIconButton(
            onClick = onMoreMenuClick,
            iconResourceId = R.drawable.ic_ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryBottomSheetHeaderPreview() {
    MnTheme {
        CategoryBottomSheetHeader(
            onEditClick = {},
            onMoreMenuClick = {},
        )
    }
}
