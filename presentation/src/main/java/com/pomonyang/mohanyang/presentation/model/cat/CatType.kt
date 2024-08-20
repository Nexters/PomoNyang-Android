package com.pomonyang.mohanyang.presentation.model.cat

import androidx.compose.runtime.Immutable
import com.mohanyang.presentation.R
import kotlin.random.Random

@Immutable
enum class CatType(
    val personality: Int,
    val personalityIcon: Int,
    val timerEndPushContent: Int,
    val restEndPushContent: Int,
    val backgroundPushContent: Int,
    val messages: List<String>,
    val onBoardingRiveCat: String,
    val pomodoroRiveCat: String,
    val catFireInput: String
) {
    CHEESE(
        personality = R.string.cat_cheese_personality,
        personalityIcon = R.drawable.ic_star,
        timerEndPushContent = R.string.cat_cheese_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_cheese_background_push,
        onBoardingRiveCat = "stretch_Cheese Cat",
        pomodoroRiveCat = "cheeseCat",
        messages = listOf(
            "나랑 함께할 시간이다냥!",
            "자주 와서 쓰다듬어 달라냥",
            "집중이 잘 될 거 같다냥"
        ),
        catFireInput = "Click_Cheese Cat"
    ),
    BLACK(
        personality = R.string.cat_black_personality,
        personalityIcon = R.drawable.ic_heart,
        timerEndPushContent = R.string.cat_black_timer_end_push,
        restEndPushContent = R.string.cat_black_rest_end_push,
        backgroundPushContent = R.string.cat_black_background_push,
        onBoardingRiveCat = "stretch_Black Cat",
        pomodoroRiveCat = "blackCat",
        messages = listOf(
            "나랑 함께할 시간이다냥!",
            "자주 와서 쓰다듬어 달라냥",
            "집중이 잘 될 거 같다냥"
        ),
        catFireInput = "Click_Black Cat"
    ),
    THREE_COLOR(
        personality = R.string.cat_three_personality,
        personalityIcon = R.drawable.ic_focus,
        timerEndPushContent = R.string.cat_three_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_three_background_push,
        onBoardingRiveCat = "stretch_Calico Cat",
        pomodoroRiveCat = "calicoCat",
        messages = listOf(
            "“시간이 없어서\"는 변명이다냥",
            "휴대폰 그만보고 집중하라냥",
            "기회란 금새 왔다 사라진다냥"
        ),
        catFireInput = "Click_Calico Cat"
    );

    fun getRandomMessage(): String = messages[Random.nextInt(messages.size)]

    companion object {
        fun safeValueOf(type: String, default: CatType = CHEESE): CatType = try {
            CatType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
