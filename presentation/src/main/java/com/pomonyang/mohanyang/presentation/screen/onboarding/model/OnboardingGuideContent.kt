package com.pomonyang.mohanyang.presentation.screen.onboarding.model

import android.content.Context
import com.mohanyang.presentation.R
import kotlin.math.max

data class OnboardingGuideContent(val title: String, val subtitle: String)

fun Context.getOnBoardingContents(): List<OnboardingGuideContent> {
    val guides = ArrayList<OnboardingGuideContent>()

    val guideTitles = this.resources.getStringArray(R.array.onboarding_guide_title)

    val guideSubTitles = this.resources.getStringArray(R.array.onboarding_guide_subtitle)

    for (index in 0 until max(guideTitles.size, guideSubTitles.size)) {
        val title = if (index < guideTitles.size) guideTitles[index] else ""
        val subtitle = if (index < guideSubTitles.size) guideSubTitles[index] else ""
        guides.add(OnboardingGuideContent(title, subtitle))
    }

    return guides
}
