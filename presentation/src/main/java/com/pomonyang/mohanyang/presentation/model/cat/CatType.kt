package com.pomonyang.mohanyang.presentation.model.cat

import androidx.compose.runtime.Immutable
import com.mohanyang.presentation.R

@Immutable
enum class CatType(
    val personality: Int,
    val personalityIcon: Int,
    val timerEndPushContent: Int,
    val restEndPushContent: Int,
    val backgroundPushContent: Int,
    val onBoardingRiveCat: String,
    val homeRiveCat: String,
    val restRiveCat: String,
    val focusRiveCat: String
) {
    CHEESE(
        personality = R.string.cat_cheese_personality,
        personalityIcon = R.drawable.ic_star,
        timerEndPushContent = R.string.cat_cheese_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_cheese_background_push,
        onBoardingRiveCat = "stretch_Cheese Cat",
        homeRiveCat = "Home_Default_Cheese Cat",
        restRiveCat = "stretch_Cheese Cat",
        focusRiveCat = "Focus_Cheese Cat"
    ),
    BLACK(
        personality = R.string.cat_black_personality,
        personalityIcon = R.drawable.ic_heart,
        timerEndPushContent = R.string.cat_black_timer_end_push,
        restEndPushContent = R.string.cat_black_rest_end_push,
        backgroundPushContent = R.string.cat_black_background_push,
        onBoardingRiveCat = "stretch_Black Cat",
        homeRiveCat = "Home_Default_Black Cat",
        restRiveCat = "stretch_Black Cat",
        focusRiveCat = "Focus_Black Cat"
    ),
    THREE_COLOR(
        personality = R.string.cat_three_personality,
        personalityIcon = R.drawable.ic_focus,
        timerEndPushContent = R.string.cat_three_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_three_background_push,
        onBoardingRiveCat = "stretch_Calico Cat",
        homeRiveCat = "Home_Default_Calico Cat",
        restRiveCat = "stretch_Calico Cat",
        focusRiveCat = "Focus_Calico Cat"
    );

    companion object {
        fun safeValueOf(type: String, default: CatType = CHEESE): CatType = try {
            CatType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
