package com.pomonyang.mohanyang.presentation.screen.statistics.component.datepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pomonyang.mohanyang.presentation.designsystem.datepicker.MnDatePickerDialog
import com.pomonyang.mohanyang.presentation.screen.statistics.component.StatisticsTopBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticDatePickerModal(
    statisticDateState: StatisticDateState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = statisticDateState.selectedDateMillis,
        selectableDates = statisticDateState as SelectableDates,
    )

    MnDatePickerDialog(
        datePickerState = datePickerState,
        onDateSelected = onDateSelected,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun StatisticDatePickerModalPreview() {
    val joinedDate = LocalDate.of(2025, 6, 1)

    val dateState = rememberStatisticDateState(joinedDate)

    MnTheme {
        StatisticDatePickerModal(
            statisticDateState = dateState,
            onDateSelected = { date -> date?.let { dateState.updateDate(it) } },
            onDismiss = {
            },
        )
    }
}

@Preview
@Composable
private fun StatisticDatePickerModalApplyPreview() {
    val joinedDate = LocalDate.of(2025, 5, 10)

    var isShowDatePicker by remember { mutableStateOf(false) }
    val dateState = rememberStatisticDateState(joinedDate)

    MnTheme {
        StatisticsTopBar(
            month = dateState.selectedMonth.toString(),
            day = dateState.selectedDay.toString(),
            onLeftClick = dateState.moveToPreviousDay,
            onRightClick = dateState.moveToNextDay,
            onMoreClick = { isShowDatePicker = true },
            isLeftClickable = dateState.canMoveToPreviousDay,
            isRightClickable = dateState.canMoveToNextDay,
        )

        if (isShowDatePicker) {
            StatisticDatePickerModal(
                statisticDateState = dateState,
                onDateSelected = { date -> date?.let { dateState.updateDate(it) } },
                onDismiss = {
                    isShowDatePicker = false
                },
            )
        }
    }
}
