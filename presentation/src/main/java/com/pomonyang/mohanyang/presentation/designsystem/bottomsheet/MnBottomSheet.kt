package com.pomonyang.mohanyang.presentation.designsystem.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectListItem
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MnBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    textStyles: MnBottomSheetTextStyles = MnBottomSheetDefaults.textStyles(),
    colors: MnBottomSheetColors = MnBottomSheetDefaults.colors(),
    headerContents: @Composable () -> Unit = {},
    contents: @Composable () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        containerColor = colors.containerColor,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest,
    ) {
        BottomSheetContent(
            title = title,
            subTitle = subTitle,
            textStyles = textStyles,
            colors = colors,
            headerContents = headerContents,
            contents = contents,
        )
    }
}

@Composable
private fun BottomSheetContent(
    textStyles: MnBottomSheetTextStyles,
    colors: MnBottomSheetColors,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    headerContents: @Composable () -> Unit,
    contents: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            title?.let {
                Text(
                    text = it,
                    style = textStyles.titleTextStyle,
                    color = colors.titleColor,
                    modifier = Modifier.padding(start = MnSpacing.xLarge),
                    maxLines = 1,
                )
            }
            headerContents()
        }

        subTitle?.let {
            Text(
                text = it,
                maxLines = 2,
                style = textStyles.subTitleTextStyle,
                color = colors.subTitleColor,
                modifier = Modifier.padding(start = MnSpacing.xLarge),
            )
        }

        Spacer(Modifier.height(MnSpacing.xLarge))

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
            headerContents = {
                MnMediumIcon(resourceId = R.drawable.ic_close)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MnSpacing.medium),
            ) {
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "기본",
                    onClick = {},
                    isSelected = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MnSpacing.small),
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "공부",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MnSpacing.small),
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "작업",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MnSpacing.small),
                )
                MnSelectListItem(
                    iconResource = R.drawable.ic_null,
                    categoryType = "운동",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MnSpacing.small),
                )

                MnBoxButton(
                    containerPadding = PaddingValues(top = MnSpacing.medium),
                    modifier = Modifier.fillMaxWidth(),
                    styles = MnBoxButtonStyles.large,
                    text = "확인",
                    onClick = { },
                    colors = MnBoxButtonColorType.secondary,
                    rightIconResourceId = R.drawable.ic_null,
                    leftIconResourceId = R.drawable.ic_null,
                )
                Spacer(Modifier.height(MnSpacing.medium))
            }
        }
    }
}
