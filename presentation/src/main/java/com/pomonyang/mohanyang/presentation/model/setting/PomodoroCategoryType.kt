package com.pomonyang.mohanyang.presentation.model.setting

import com.mohanyang.presentation.R

enum class PomodoroCategoryType(val iconRes: Int) {
    DEFAULT(R.drawable.ic_null),
    READ_BOOK(R.drawable.ic_null),
    STUDY(R.drawable.ic_null),
    WORK(R.drawable.ic_null);

    companion object {
        fun safeValueOf(title: String): PomodoroCategoryType {
            // TODO 추후에 서버에서 Icon까지 내려주면 이렇게 바꿀 필요가 없음
            return when (title.lowercase()) {
                "독서" -> READ_BOOK
                "공부" -> STUDY
                "작업" -> WORK
                else -> DEFAULT
            }
        }
    }
}
