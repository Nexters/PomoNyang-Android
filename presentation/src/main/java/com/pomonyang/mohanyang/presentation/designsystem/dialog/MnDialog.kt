package com.pomonyang.mohanyang.presentation.designsystem.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun MnDialog(
    title: String,
    modifier: Modifier = Modifier,
    dialogColors: MnDialogColors = MnDialogDefaults.colors(),
    dialogTextStyles: MnDialogTextStyles = MnDialogDefaults.textStyles(),
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = true
    ),
    subTitle: String? = null,
    // TODO 나중에 MnButton완성 되면 거기에 필요한 leading / action / text 갖고 있는 클래스 사용
    positiveButtonLabel: String? = null,
    negativeButtonLabel: String? = null,
    onPositiveButtonClick: () -> Unit = {},
    onNegativeButtonClick: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    MnTheme {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                modifier = modifier.width(MnDialogDefaults.containerWidth),
                shape = RoundedCornerShape(MnRadius.medium),
                color = dialogColors.containerColor
            ) {
                Column(
                    modifier = Modifier.padding(MnSpacing.xLarge)
                ) {
                    DialogTitle(
                        title = title,
                        dialogTextStyles = dialogTextStyles,
                        dialogColors = dialogColors
                    )
                    DialogDescription(
                        subTitle = subTitle,
                        dialogTextStyles = dialogTextStyles,
                        dialogColors = dialogColors
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MnSpacing.medium),
                        modifier = Modifier.padding(top = MnSpacing.large)
                    ) {
                        PositiveButton(
                            modifier = Modifier.weight(1f),
                            positiveButtonLabel = positiveButtonLabel,
                            dialogColors = dialogColors,
                            onPositiveButtonClick = onPositiveButtonClick,
                            dialogTextStyles = dialogTextStyles
                        )
                        NegativeButton(
                            modifier = Modifier.weight(1f),
                            negativeButtonLabel = negativeButtonLabel,
                            dialogColors = dialogColors,
                            onNegativeButtonClick = onNegativeButtonClick,
                            dialogTextStyles = dialogTextStyles
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogDescription(
    subTitle: String?,
    dialogTextStyles: MnDialogTextStyles,
    dialogColors: MnDialogColors
) {
    subTitle?.let {
        Text(
            text = it,
            maxLines = 2,
            style = dialogTextStyles.subTitleTextStyle,
            color = dialogColors.subTitleColor
        )
    }
}

@Composable
private fun DialogTitle(
    title: String,
    dialogTextStyles: MnDialogTextStyles,
    dialogColors: MnDialogColors
) {
    Text(
        text = title,
        style = dialogTextStyles.titleTextStyle,
        color = dialogColors.titleColor,
        modifier = Modifier.padding(vertical = 7.5.dp),
        maxLines = 1
    )
}

@Composable
private fun NegativeButton(
    negativeButtonLabel: String?,
    dialogColors: MnDialogColors,
    onNegativeButtonClick: () -> Unit,
    dialogTextStyles: MnDialogTextStyles,
    modifier: Modifier = Modifier
) {
    negativeButtonLabel?.let {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = dialogColors.negativeButtonContainerColor),
            onClick = onNegativeButtonClick,
            modifier = modifier
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides dialogTextStyles.negativeButtonTextStyle,
                LocalContentColor provides dialogColors.negativeButtonTextColor
            ) {
                Text(text = negativeButtonLabel)
            }
        }
    }
}

@Composable
private fun PositiveButton(
    positiveButtonLabel: String?,
    dialogColors: MnDialogColors,
    onPositiveButtonClick: () -> Unit,
    dialogTextStyles: MnDialogTextStyles,
    modifier: Modifier = Modifier
) {
    positiveButtonLabel?.let {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = dialogColors.positiveButtonContainerColor),
            onClick = onPositiveButtonClick,
            modifier = modifier
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides dialogTextStyles.positiveButtonTextStyle,
                LocalContentColor provides dialogColors.positiveButtonTextColor
            ) {
                Text(text = positiveButtonLabel)
            }
        }
    }
}

@Preview
@Composable
private fun MnDialogPreview() {
    MnTheme {
        MnDialog(
            title = "Dialog Title",
            subTitle = "Dialog Subtext를 입력해주세요.\n" +
                "최대 2줄을 넘지 않도록 해요.",
            positiveButtonLabel = "Button",
            negativeButtonLabel = "Button",
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun MnDialogOnlyPositiveButtonPreview() {
    MnTheme {
        MnDialog(
            title = "Dialog Title",
            subTitle = "Dialog Subtext를 입력해주세요.\n" +
                "최대 2줄을 넘지 않도록 해요.",
            positiveButtonLabel = "Button",
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun MnDialogOnlyNegativeButtonPreview() {
    MnTheme {
        MnDialog(
            title = "Dialog Title",
            subTitle = "Dialog Subtext를 입력해주세요.\n" +
                "최대 2줄을 넘지 않도록 해요.",
            negativeButtonLabel = "Button",
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun MnDialogNoSubTitlePreview() {
    MnTheme {
        MnDialog(
            title = "Dialog Title",
            positiveButtonLabel = "Button",
            negativeButtonLabel = "Button",
            onDismissRequest = {}
        )
    }
}
