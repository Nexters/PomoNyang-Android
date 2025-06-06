package com.pomonyang.mohanyang.presentation.designsystem.datepicker

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnDatePickerDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun defaultDatePickerColor() = DatePickerDefaults.colors(
        containerColor = MnTheme.backgroundColorScheme.secondary,
        titleContentColor = MnTheme.textColorScheme.primary,
        headlineContentColor = MnTheme.textColorScheme.primary,
        weekdayContentColor = MnTheme.textColorScheme.primary,
        subheadContentColor = MnTheme.textColorScheme.primary,
        navigationContentColor = MnTheme.iconColorScheme.secondary,
        yearContentColor = MnTheme.textColorScheme.tertiary,
        disabledYearContentColor = MnTheme.textColorScheme.disabled,
        currentYearContentColor = MnTheme.textColorScheme.primary,
        selectedYearContentColor = MnTheme.backgroundColorScheme.secondary,
        disabledSelectedYearContentColor = MnTheme.textColorScheme.disabled,
        selectedYearContainerColor = MnTheme.backgroundColorScheme.accent1,
        disabledSelectedYearContainerColor = MnTheme.textColorScheme.disabled,
        dayContentColor = MnTheme.textColorScheme.primary,
        disabledDayContentColor = MnTheme.textColorScheme.disabled,
        selectedDayContentColor = MnTheme.backgroundColorScheme.secondary,
        disabledSelectedDayContentColor = MnTheme.textColorScheme.disabled,
        selectedDayContainerColor = MnTheme.backgroundColorScheme.accent1,
        disabledSelectedDayContainerColor = MnTheme.textColorScheme.disabled,
        todayContentColor = MnTheme.textColorScheme.primary,
        todayDateBorderColor = MnTheme.backgroundColorScheme.inverse,
        dayInSelectionRangeContentColor = Color.Unspecified,
        dayInSelectionRangeContainerColor = Color.Unspecified,
        dividerColor = MnTheme.backgroundColorScheme.secondary,
        dateTextFieldColors = OutlinedTextFieldDefaults.colors(
            cursorColor = MnColor.Orange500,
            selectionColors = TextSelectionColors(
                handleColor = MnColor.Orange500,
                backgroundColor = MnTheme.backgroundColorScheme.secondary,
            ),
            focusedPrefixColor = MnColor.Orange500,
            focusedBorderColor = MnColor.Orange500,
            focusedLabelColor = MnColor.Orange500,
            focusedTextColor = MnTheme.textColorScheme.primary,
            unfocusedPrefixColor = MnTheme.textColorScheme.primary,
            unfocusedTextColor = MnTheme.textColorScheme.primary,
            unfocusedLabelColor = MnTheme.textColorScheme.primary,
            focusedContainerColor = MnTheme.backgroundColorScheme.secondary,
            unfocusedContainerColor = MnTheme.backgroundColorScheme.secondary,
        ),
    )
}
