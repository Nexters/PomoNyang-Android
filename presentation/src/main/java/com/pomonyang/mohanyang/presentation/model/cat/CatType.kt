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
    val pomodoroRiveCat: String
) {
    CHEESE(
        personality = R.string.cat_cheese_personality,
        personalityIcon = R.drawable.ic_star,
        timerEndPushContent = R.string.cat_cheese_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_cheese_background_push,
        onBoardingRiveCat = "stretch_Cheese Cat",
        pomodoroRiveCat = "cheeseCat"
    ),
    BLACK(
        personality = R.string.cat_black_personality,
        personalityIcon = R.drawable.ic_heart,
        timerEndPushContent = R.string.cat_black_timer_end_push,
        restEndPushContent = R.string.cat_black_rest_end_push,
        backgroundPushContent = R.string.cat_black_background_push,
        onBoardingRiveCat = "stretch_Black Cat",
        pomodoroRiveCat = "blackCat"
    ),
    THREE_COLOR(
        personality = R.string.cat_three_personality,
        personalityIcon = R.drawable.ic_focus,
        timerEndPushContent = R.string.cat_three_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_three_background_push,
        onBoardingRiveCat = "stretch_Calico Cat",
        pomodoroRiveCat = "calicoCat"
    );

    companion object {
        fun safeValueOf(type: String, default: CatType = CHEESE): CatType = try {
            CatType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
