package com.pomonyang.mohanyang.domain.model.cat

import androidx.compose.runtime.Immutable
import com.mohanyang.domain.R
import kotlinx.serialization.Serializable

@Immutable
@Serializable
enum class CatType(
    val kor: Int,
    val personality: Int,
    val personalityIcon: Int,
    val timerEndPushContent: Int,
    val restEndPushContent: Int,
    val backgroundPushContent: Int

) {
    CHEESE(
        kor = R.string.cat_cheese_kor,
        personality = R.string.cat_cheese_personality,
        personalityIcon = R.drawable.ic_star,
        timerEndPushContent = R.string.cat_cheese_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_cheese_background_push
    ),
    BLACK(
        kor = R.string.cat_black_kor,
        personality = R.string.cat_black_personality,
        personalityIcon = R.drawable.ic_heart,
        timerEndPushContent = R.string.cat_black_timer_end_push,
        restEndPushContent = R.string.cat_black_rest_end_push,
        backgroundPushContent = R.string.cat_black_background_push
    ),
    THREE_COLOR(
        kor = R.string.cat_three_kor,
        personality = R.string.cat_three_personality,
        personalityIcon = R.drawable.ic_focus,
        timerEndPushContent = R.string.cat_three_timer_end_push,
        restEndPushContent = R.string.cat_cheese_rest_end_push,
        backgroundPushContent = R.string.cat_three_background_push
    );

    companion object {
        fun safeValueOf(type: String, default: CatType = CHEESE): CatType = try {
            CatType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
