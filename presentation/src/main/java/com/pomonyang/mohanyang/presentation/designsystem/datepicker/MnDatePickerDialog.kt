package com.pomonyang.mohanyang.presentation.designsystem.datepicker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MnDatePickerDialog(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DatePickerDefaults.shape,
    tonalElevation: Dp = DatePickerDefaults.TonalElevation,
    colors: DatePickerColors = MnDatePickerDefaults.defaultDatePickerColor(),
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            MnTextButton(
                text = stringResource(id = R.string.confirm),
                styles = MnTextButtonStyles.medium,
                textColor = MnColor.Orange500,
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
            )
        },
        dismissButton = {
            MnTextButton(
                text = stringResource(id = R.string.common_cancel),
                styles = MnTextButtonStyles.medium,
                onClick = {
                    onDismiss()
                },
            )
        },
        tonalElevation = tonalElevation,
        shape = shape,
        colors = colors,
        properties = properties,
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier,
            colors = colors,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewMnDatePickerDialog() {
    MnTheme {
        MnDatePickerDialog(
            datePickerState = DatePickerState(locale = Locale.KOREAN),
            onDateSelected = {},
            onDismiss = {},
            modifier = Modifier,
        )
    }
}
