import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.pomonyang.mohanyang.presentation.designsystem.button.toggle.MnToggleButtonSize
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.noRippleClickable
import kotlin.math.roundToInt

@Composable
fun MnToggleButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    val minBound = with(density) { MnToggleButtonSize.padding.toPx() }
    val maxBound = with(density) { (MnToggleButtonSize.width - MnToggleButtonSize.thumbSize - MnToggleButtonSize.padding).toPx() }

    val slide by animateFloatAsState(
        targetValue = if (isChecked) maxBound else minBound,
        animationSpec = tween(durationMillis = 200),
        label = "mn_switch"
    )

    Box(
        modifier = modifier
            .size(
                width = MnToggleButtonSize.width,
                height = MnToggleButtonSize.height
            )
            .clip(RoundedCornerShape(MnRadius.max))
            .background(
                if (isChecked) {
                    MnTheme.backgroundColorScheme.accent1
                } else {
                    MnTheme.iconColorScheme.disabled
                }
            )
            .noRippleClickable(onClick = { onCheckedChange(!isChecked) })

    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        slide.roundToInt(),
                        MnToggleButtonSize.padding
                            .toPx()
                            .roundToInt()
                    )
                }
                .size(MnToggleButtonSize.thumbSize)
                .clip(CircleShape)
                .background(MnTheme.iconColorScheme.inverse)
        )
    }
}

@ThemePreviews
@Composable
fun PreviewMnToggleButton() {
    var checked by remember { mutableStateOf(false) }

    MnToggleButton(isChecked = checked) {
        checked = it
    }
}
