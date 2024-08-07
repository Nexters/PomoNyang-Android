package com.pomonyang.mohanyang.presentation.designsystem.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun MnTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    textColor: Color = MnTheme.textColorScheme.primary,
    textStyle: TextStyle = MnTheme.typography.subBodySemiBold,
    counterMaxLength: Int = 0,
    readOnly: Boolean = false,
    isEnabled: Boolean = true,
    hint: String = "",
    hintTextStyle: TextStyle = MnTheme.typography.bodySemiBold,
    isError: Boolean = false,
    errorMessage: String = "",
    errorTextStyle: TextStyle = MnTheme.typography.captionRegular,
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = MnColor.Orange500,
    borderWidth: Dp = 1.dp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {
            keyboardController?.hide()
            focusManager.clearFocus(true)
        }
    ),
    textSelectionColors: TextSelectionColors = TextSelectionColors(
        handleColor = MnTheme.backgroundColorScheme.accent1,
        backgroundColor = Color.Transparent
    )
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    CompositionLocalProvider(
        LocalTextSelectionColors provides textSelectionColors
    ) {
        Column(modifier = modifier) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {
                    val newValue = if (counterMaxLength > 0) {
                        it.take(counterMaxLength)
                    } else {
                        it
                    }
                    onValueChange(newValue)
                },

                enabled = isEnabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(textColor),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = isSingleLine,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(MnTheme.backgroundColorScheme.accent1),
                decorationBox = @Composable { innerTextField ->
                    Surface(
                        color = backgroundColor,
                        border = BorderStroke(
                            borderWidth,
                            if (isFocused || isError) borderColor else backgroundColor
                        ),
                        shape = RoundedCornerShape(MnSpacing.small)

                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(MnSpacing.small))
                                .background(backgroundColor)
                                .padding(MnSpacing.large),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (!isFocused && value.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = MnTheme.textColorScheme.disabled,
                                    style = hintTextStyle
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
            if (isError && errorMessage.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = MnSpacing.xSmall,
                            start = MnSpacing.xSmall
                        ),
                    text = errorMessage,
                    style = errorTextStyle,
                    color = MnColor.Red300
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMnTextField() {
    MnTheme {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        val text = remember {
            mutableStateOf("모하냥")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                }
        ) {
            MnTextField(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MnColor.White,
                textStyle = MnTheme.typography.header4,
                value = text.value,
                onValueChange = { value -> text.value = value },
                hint = "hint",
                isEnabled = true,
                isError = true,
                textColor = MnColor.Gray400,
                errorMessage = "에러났다냥"
            )
        }
    }
}