package com.pomonyang.mohanyang.presentation.screen.home.category.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.dropShadow

@Composable
fun CategoryActionMoreMenuList(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val blurSpreadSize = 8.dp
    Box(
        modifier =
        Modifier
            .padding(blurSpreadSize)
            .wrapContentSize(align = Alignment.Center)
    ) {
        Column(
            modifier = modifier
                .dropShadow(
                    shape = RoundedCornerShape(MnRadius.small),
                    blur = blurSpreadSize,
                    offsetY = 2.dp,
                )
                .background(color = MnColor.White, shape = RoundedCornerShape(MnRadius.small))
                .padding(vertical = MnSpacing.medium, horizontal = MnSpacing.small),
        ) {
            CategoryActionMenu(
                resourceId = R.drawable.ic_pen,
                onClick = onEditClick,
                text = stringResource(R.string.category_setting_edit),
            )
            CategoryActionMenu(
                resourceId = R.drawable.ic_trashcan,
                onClick = onDeleteClick,
                text = stringResource(R.string.category_setting_delete),
            )
        }
    }
}



@Composable
private fun CategoryActionMenu(
    @DrawableRes resourceId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .padding(start = MnSpacing.medium, end = MnSpacing.large)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MnMediumIcon(
            tint = MnTheme.iconColorScheme.primary,
            resourceId = resourceId,
        )
        Text(
            text = text,
            style = MnTheme.typography.bodySemiBold,
            color = MnTheme.textColorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryManagementControlsPreview() {
    MnTheme {
        Box(
            Modifier
                .size(500.dp)
                .background(Color.Gray),
        ) {
            CategoryActionMoreMenuList(
                onDeleteClick = {},
                onEditClick = {},
            )
        }
    }
}
