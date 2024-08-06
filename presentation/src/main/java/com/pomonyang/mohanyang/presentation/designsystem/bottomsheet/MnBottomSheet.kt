package com.pomonyang.mohanyang.presentation.designsystem.bottomsheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MnBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    buttonText: String? = null,
    @DrawableRes buttonRightIconResourceId: Int? = null,
    @DrawableRes buttonLeftIconResourceId: Int? = null,
    textStyles: MnBottomSheetTextStyles = MnBottomSheetDefaults.textStyles(),
    colors: MnBottomSheetColors = MnBottomSheetDefaults.colors(),
    contents: @Composable () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        containerColor = colors.containerColor,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest
    ) {
        BottomSheetContent(
            title = title,
            subTitle = subTitle,
            textStyles = textStyles,
            colors = colors
        ) {
            Column(modifier = Modifier.padding(horizontal = MnSpacing.large)) {
                contents()
                buttonText?.let {
                    Spacer(Modifier.height(MnSpacing.large))
                    MnBoxButton(
                        modifier = Modifier.fillMaxWidth(),
                        styles = MnBoxButtonStyles.large,
                        text = it,
                        onClick = { },
                        colors = MnBoxButtonColorType.secondary,
                        rightIconResourceId = buttonRightIconResourceId,
                        leftIconResourceId = buttonLeftIconResourceId
                    )
                    Spacer(Modifier.height(MnSpacing.medium))
                }
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    textStyles: MnBottomSheetTextStyles,
    colors: MnBottomSheetColors,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    contents: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        title?.let {
            Text(
                text = it,
                style = textStyles.titleTextStyle,
                color = colors.titleColor,
                modifier = Modifier.padding(start = 20.dp),
                maxLines = 1
            )
        }

        subTitle?.let {
            Text(
                text = it,
                maxLines = 2,
                style = textStyles.subTitleTextStyle,
                color = colors.subTitleColor,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        contents()
    }
}

@Composable
@Preview
private fun MnModalBottomSheetPreview() {
    MnTheme {
        MnBottomSheet(
            onDismissRequest = {},
            modifier = Modifier.fillMaxWidth(),
            title = "Dialog Title",
            subTitle = "Dialog SubText를 입력해주세요.\n최대 2줄을 넘지 않도록 해요.",
            buttonText = "확인"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
                modifier = Modifier.fillMaxWidth()
            ) {
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "기본",
                    onClick = {},
                    isSelected = true,
                    restTime = "25",
                    focusTime = "25",
                    modifier = Modifier.fillMaxWidth()
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "공부",
                    onClick = {},
                    restTime = "25",
                    focusTime = "25",
                    modifier = Modifier.fillMaxWidth()
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "작업",
                    onClick = {},
                    restTime = "25",
                    focusTime = "25",
                    modifier = Modifier.fillMaxWidth()
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "운동",
                    onClick = {},
                    restTime = "25",
                    focusTime = "25",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
