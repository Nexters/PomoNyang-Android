package com.pomonyang.mohanyang.presentation.designsystem.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.icon.MnXSmallIcon
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun MnHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    titleTrailingIcon: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(
            start = MnSpacing.xLarge,
        ),
        verticalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
    ) {
        Row {
            Text(
                text = title,
                style = MnTheme.typography.header3,
                color = MnTheme.textColorScheme.primary,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )

            titleTrailingIcon?.let {
                titleTrailingIcon()
                Spacer(modifier = Modifier.width(MnSpacing.small))
            }
        }
        description?.let {
            Text(
                text = it,
                style = MnTheme.typography.bodyRegular,
                color = MnTheme.textColorScheme.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        }
    }
}

@Composable
@ThemePreviews
private fun MnHeaderPreview() {
    MnTheme {
        MnHeader(
            title = "Title",
            description = "보조설명을 입력해주세요.\n최대 2줄을 넘지 않도록 해요.",
            titleTrailingIcon = {
                MnXSmallIcon(
                    resourceId = R.drawable.ic_null,
                    modifier = Modifier.padding(
                        MnSpacing.small,
                    ),
                )
                MnXSmallIcon(
                    resourceId = R.drawable.ic_null,
                    modifier = Modifier.padding(
                        MnSpacing.small,
                    ),
                )
                MnXSmallIcon(
                    resourceId = R.drawable.ic_null,
                    modifier = Modifier.padding(
                        MnSpacing.small,
                    ),
                )
            },
        )
    }
}
