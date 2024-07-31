import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.icon.MNLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MNMediumIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MNSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MNXLargeIcon
import com.pomonyang.mohanyang.presentation.designsystem.icon.MNXSmallIcon
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
            MNXSmallIcon()
            MNSmallIcon()
            MNMediumIcon()
            MNLargeIcon()
            MNXLargeIcon()
        }
    }
}
