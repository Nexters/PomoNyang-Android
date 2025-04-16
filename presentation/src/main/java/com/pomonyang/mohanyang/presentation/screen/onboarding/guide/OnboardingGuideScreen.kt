package com.pomonyang.mohanyang.presentation.screen.onboarding.guide

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.screen.onboarding.model.OnboardingGuideContent
import com.pomonyang.mohanyang.presentation.screen.onboarding.model.getOnBoardingContents
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.clickableSingle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun OnboardingGuideRoute(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingGuideViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        onboardingViewModel.handleEvent(OnboardingGuideEvent.Init)
    }

    OnboardingGuideScreen(
        modifier = modifier,
        onHomeClick = onStartClick,
    )
}

@Composable
private fun OnboardingGuideScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
) {
    val onboardingContents = LocalContext.current.getOnBoardingContents()

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(MnTheme.backgroundColorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OnboardingSlider(
            contents = onboardingContents,
        )
        MnBoxButton(
            modifier = Modifier.width(200.dp),
            text = LocalContext.current.getString(R.string.onboarding_start),
            onClick = onHomeClick,
            colors = MnBoxButtonColorType.primary,
            styles = MnBoxButtonStyles.large,
        )
    }
}

@Composable
fun OnboardingSlider(
    contents: List<OnboardingGuideContent>,
    modifier: Modifier = Modifier,
) {
    val infinitePageCount = Int.MAX_VALUE

    val pagerState = rememberPagerState(pageCount = { infinitePageCount })

    val scope = rememberCoroutineScope()

    val animationDelay = 3000L
    val animationDuration = 500

    suspend fun onboardingContentScroll(page: Int) {
        pagerState.animateScrollToPage(
            page = page,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(animationDelay)
            val nextPage = pagerState.currentPage + 1
            onboardingContentScroll(nextPage)
        }
    }

    Column(
        modifier = modifier.padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            HorizontalPager(
                state = pagerState,
            ) { currentPage ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OnboardingGuideContent(
                        content = contents[currentPage % contents.size],
                    )
                }
            }
        }

        PageIndicator(
            pageCount = contents.size,
            currentPage = pagerState.currentPage % contents.size,
            onClickIndicator = { page ->
                scope.launch {
                    onboardingContentScroll(page)
                }
            },
        )
    }
}

@Composable
private fun OnboardingGuideContent(
    content: OnboardingGuideContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(bottom = MnSpacing.threeXLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MnSpacing.threeXLarge),
    ) {
        Image(
            painter = painterResource(id = content.image),
            contentDescription = stringResource(R.string.onboarding_guide_image),
            modifier = Modifier.size(240.dp),
        )
        TextGuide(content)
    }
}

@Composable
private fun TextGuide(
    content: OnboardingGuideContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MnSpacing.small),
    ) {
        Text(
            text = content.title,
            style = MnTheme.typography.header4,
            textAlign = TextAlign.Center,
            color = MnTheme.textColorScheme.primary,
        )
        Text(
            text = content.subtitle,
            style = MnTheme.typography.bodyRegular,
            textAlign = TextAlign.Center,
            color = MnTheme.textColorScheme.secondary,
        )
    }
}

@Composable
private fun IndicatorDots(
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickableSingle(activeRippleEffect = false) { onClick() }
            .clip(CircleShape)
            .size(8.dp)
            .background(
                if (isSelected) MnTheme.backgroundColorScheme.tertiary else MnTheme.backgroundColorScheme.secondary,
            ),
    )
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    onClickIndicator: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MnSpacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) {
            IndicatorDots(
                isSelected = it == currentPage,
                onClick = { onClickIndicator(it) },
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingContentPreview() {
    OnboardingGuideContent(
        content = OnboardingGuideContent(
            title = "모하냥과 함께 집중시간을 늘려보세요",
            subtitle = "고양이 종에 따라 성격이 달라요\n" +
                "취향에 맞는 고양이를 선택해 몰입해 보세요",
            image = R.drawable.onboarding_contents_1,
        ),
    )
}

@DevicePreviews
@Composable
private fun OnboardingScreenPreview() {
    OnboardingGuideScreen {}
}
