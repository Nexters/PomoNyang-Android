package com.pomonyang.mohanyang.presentation.screen.onboarding.model

import android.content.Context
import com.mohanyang.presentation.R
import kotlin.math.max

data class OnboardingGuideContent(val title: String, val subtitle: String)

fun Context.getOnBoardingContents(): List<OnboardingGuideContent> {
    val guideTitles = this.resources.getStringArray(R.array.onboarding_guide_title)

    val guideSubTitles = this.resources.getStringArray(R.array.onboarding_guide_subtitle)

    val maxSize = max(guideTitles.size, guideSubTitles.size)

    val guides = List(maxSize) { index ->
        val title = guideTitles.getOrElse(index) { "" }
        val subtitle = guideSubTitles.getOrElse(index) { "" }
        OnboardingGuideContent(title, subtitle)
    }

    return guides
}
