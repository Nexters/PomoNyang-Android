package com.pomonyang.mohanyang.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.select.MnSelectButton
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun TimerSelectedButtons(
    modifier: Modifier = Modifier,
    title: String,
    onPlusButtonClick: () -> Unit,
    onMinusButtonClick: () -> Unit,
    plusButtonSelected: Boolean,
    minusButtonSelected: Boolean,
    plusButtonEnabled: Boolean,
    minusButtonEnabled: Boolean
) {
    Text(
        modifier = Modifier.padding(top = MnSpacing.xLarge),
        text = title,
        style = MnTheme.typography.bodySemiBold,
        color = MnTheme.textColorScheme.disabled
    )
    Row(
        modifier = modifier.padding(top = MnSpacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small)
    ) {
        // TODO [코니] +- 아이콘으로 적용 필요
        MnSelectButton(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.medium
            ),
            isEnabled = minusButtonEnabled,
            isSelected = minusButtonSelected,
            onClick = { onMinusButtonClick() },
            subTitleContent = {
                Text(text = "- ${stringResource(R.string.five_minutes)}")
            }
        )

        MnSelectButton(
            modifier = Modifier.padding(
                vertical = MnSpacing.small,
                horizontal = MnSpacing.medium
            ),
            isEnabled = plusButtonEnabled,
            isSelected = plusButtonSelected,
            onClick = { onPlusButtonClick() },
            subTitleContent = {
                Text(text = "+ ${stringResource(R.string.five_minutes)}")
            }
        )
    }
}

@Preview
@Composable
private fun FocusTimerSelectedButtonsPreview() {
    MnTheme {
        TimerSelectedButtons(
            title = "다음부터 휴식시간을 바꿀까요?",
            onPlusButtonClick = { },
            onMinusButtonClick = { },
            plusButtonSelected = true,
            minusButtonSelected = false,
            plusButtonEnabled = true,
            minusButtonEnabled = false

        )
    }
}
