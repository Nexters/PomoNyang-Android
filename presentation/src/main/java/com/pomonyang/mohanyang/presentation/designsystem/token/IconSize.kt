import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXSmallIcon
import com.pomonyang.mohanyang.presentation.theme.MohaNyangTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

object IconSize {
    val xSmall = 16.dp
    val small = 20.dp
    val medium = 24.dp
    val large = 32.dp
    val xLarge = 48.dp
}

@ThemePreviews
@Composable
@Preview
private fun MohaNyangIconPreview() {
    MohaNyangTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
        ) {
            MnXSmallIcon()
            MnSmallIcon()
            MnMediumIcon()
            MnLargeIcon()
            MnXLargeIcon()
        }
    }
}
