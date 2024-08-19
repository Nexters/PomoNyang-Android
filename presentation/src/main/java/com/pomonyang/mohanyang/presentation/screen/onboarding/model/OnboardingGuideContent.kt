package com.pomonyang.mohanyang.presentation.screen.onboarding.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.mohanyang.presentation.R
import kotlin.math.max

@Stable
data class OnboardingGuideContent(
    val title: String,
    val subtitle: String,
    @DrawableRes val image: Int
)

fun Context.getOnBoardingContents(): List<OnboardingGuideContent> {
    val guideTitles = this.resources.getStringArray(R.array.onboarding_guide_title)
    val guideSubTitles = this.resources.getStringArray(R.array.onboarding_guide_subtitle)
    val guideImages = this.resources.obtainTypedArray(R.array.onboarding_guide_image)

    val maxSize = max(guideTitles.size, guideSubTitles.size)

    val guides = List(maxSize) { index ->
        val title = guideTitles.getOrElse(index) { "" }
        val subtitle = guideSubTitles.getOrElse(index) { "" }
        val guideImage = guideImages.getResourceId(index, R.drawable.onboarding_contents_1)
        OnboardingGuideContent(title, subtitle, guideImage)
    }
    guideImages.recycle()

    return guides
}
