package com.pomonyang.mohanyang.presentation.screen.home.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.icon.MnIconButton
import com.pomonyang.mohanyang.presentation.designsystem.textfield.MnTextField
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnIconSize
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle

@Composable
fun CategorySettingRoute(
    categoryNo: Int?,
    modifier: Modifier = Modifier,
) {
    CategorySettingScreen(categoryNo = categoryNo)
}

@Composable
fun CategorySettingScreen(
    modifier: Modifier = Modifier,
    categoryNo: Int?,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars))
            .background(MnTheme.backgroundColorScheme.primary)
            .clickableSingle(activeRippleEffect = false) {
                focusManager.clearFocus(true)
                keyboardController?.hide()
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MnTopAppBar(
            navigationIcon = {
                MnIconButton(
                    onClick = {},
                    iconResourceId = R.drawable.ic_chevron_left,
                )
            },
            content = {
                Text(
                    text = if (categoryNo == null) stringResource(id = R.string.category_create_title) else stringResource(id = R.string.category_edit_title),
                    style = MnTheme.typography.bodySemiBold,
                    color = MnTheme.textColorScheme.primary,
                )
            },
        )

        CategorySelectIcon(
            modifier = Modifier.padding(top = MnSpacing.threeXLarge, bottom = MnSpacing.twoXLarge),
            categoryNo = 1,
            onClickEdit = {},
        )

        MnTextField(
            modifier = Modifier
                .padding(horizontal = MnSpacing.xLarge),
            backgroundColor = MnColor.White,
            textStyle = MnTheme.typography.bodySemiBold,
            value = "",
            onValueChange = {},
        )

        Spacer(modifier = Modifier.weight(1f))

        MnBoxButton(
            modifier = Modifier.fillMaxWidth(),
            containerPadding = PaddingValues(MnSpacing.xLarge),
            text = stringResource(id = R.string.complete),
            onClick = { /*TODO*/ },
            colors = MnBoxButtonColorType.primary,
            styles = MnBoxButtonStyles.large,
        )
    }
}

@Composable
private fun CategorySelectIcon(
    categoryNo: Int,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .semantics { role = Role.Button },
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MnTheme.backgroundColorScheme.secondary,
                    shape = RoundedCornerShape(MnRadius.xSmall),
                )
                .padding(MnSpacing.xLarge),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_null),
                contentDescription = "categoryIcon",
                modifier = Modifier.size(40.dp),
            )
        }
        Box(
            modifier = Modifier
                .absoluteOffset(x = 52.dp, y = 44.dp)
                .background(
                    color = MnTheme.backgroundColorScheme.inverse,
                    shape = CircleShape,
                )
                .size(36.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pen),
                contentDescription = "카테고리 수정",
                modifier = Modifier.size(MnIconSize.small),
                tint = MnColor.White,
            )
        }
    }
}

@Preview
@Composable
fun PreviewCategorySelectIcon() {
    MnTheme {
        CategorySelectIcon(categoryNo = 1, onClickEdit = {})
    }
}

@Preview
@Composable
fun PreviewCategorySettingScreen() {
    MnTheme {
        CategorySettingScreen(
            categoryNo = 1,
            modifier = Modifier,
        )
    }
}
