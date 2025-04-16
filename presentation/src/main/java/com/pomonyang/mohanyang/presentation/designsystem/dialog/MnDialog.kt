package com.pomonyang.mohanyang.presentation.designsystem.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.clickableSingle

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
        decorFitsSystemWindows = true,
    ),
    subTitle: String? = null,
    positiveButton: @Composable (() -> Unit)? = null,
    negativeButton: @Composable (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
) {
    MnTheme {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties,
        ) {
            Surface(
                modifier = modifier.width(MnDialogDefaults.containerWidth),
                shape = RoundedCornerShape(MnRadius.medium),
                color = dialogColors.containerColor,
            ) {
                Column(
                    modifier = Modifier.padding(MnSpacing.xLarge),
                ) {
                    DialogTitle(
                        title = title,
                        dialogTextStyles = dialogTextStyles,
                        dialogColors = dialogColors,
                        onCloseClick = onCloseClick?.let { closeClick ->
                            {
                                closeClick()
                                onDismissRequest()
                            }
                        },
                    )
                    DialogDescription(
                        subTitle = subTitle,
                        dialogTextStyles = dialogTextStyles,
                        dialogColors = dialogColors,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MnSpacing.medium),
                        modifier = Modifier.padding(top = MnSpacing.large),
                    ) {
                        if (positiveButton != null) {
                            Box(
                                modifier = Modifier.weight(1f),
                            ) {
                                CompositionLocalProvider(
                                    LocalTextStyle provides dialogTextStyles.positiveButtonTextStyle,
                                    LocalContentColor provides dialogColors.positiveButtonTextColor,
                                ) {
                                    positiveButton()
                                }
                            }
                        }
                        if (negativeButton != null) {
                            Box(
                                modifier = Modifier.weight(1f),
                            ) {
                                CompositionLocalProvider(
                                    LocalTextStyle provides dialogTextStyles.negativeButtonTextStyle,
                                    LocalContentColor provides dialogColors.negativeButtonTextColor,
                                ) {
                                    negativeButton()
                                }
                            }
                        }
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
    dialogColors: MnDialogColors,
) {
    subTitle?.let {
        Text(
            text = it,
            maxLines = 2,
            style = dialogTextStyles.subTitleTextStyle,
            color = dialogColors.subTitleColor,
        )
    }
}

@Composable
private fun DialogTitle(
    title: String,
    dialogTextStyles: MnDialogTextStyles,
    dialogColors: MnDialogColors,
    onCloseClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = dialogTextStyles.titleTextStyle,
            color = dialogColors.titleColor,
            modifier = Modifier.padding(vertical = 7.5.dp),
            maxLines = 1,
        )
        if (onCloseClick != null) {
            MnMediumIcon(
                resourceId = R.drawable.ic_close,
                modifier = Modifier
                    .padding(8.dp)
                    .clickableSingle(activeRippleEffect = false) { onCloseClick() },
            )
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
            positiveButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("확인")
                }
            },
            negativeButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("취소")
                }
            },
            onCloseClick = {},
            onDismissRequest = {},
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
            positiveButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("확인")
                }
            },
            onDismissRequest = {},
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
            negativeButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("취소")
                }
            },
            onDismissRequest = {},
        )
    }
}

@Preview
@Composable
private fun MnDialogNoSubTitlePreview() {
    MnTheme {
        MnDialog(
            title = "Dialog Title",
            positiveButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("확인")
                }
            },
            negativeButton = {
                // TODO 나중에 변경 MnBoxButton으로 변경
                Button(
                    onClick = {},
                ) {
                    Text("취소")
                }
            },
            onDismissRequest = {},
        )
    }
}
