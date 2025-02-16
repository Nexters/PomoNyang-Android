package com.pomonyang.mohanyang.presentation.model.setting

import com.mohanyang.presentation.R

enum class PomodoroCategoryType(
    val iconRes: Int,
    val kor: Int,
) {
    DEFAULT(
        iconRes = R.drawable.ic_category_default,
        kor = R.string.category_default_kor,
    ),
    READ_BOOK(
        iconRes = R.drawable.ic_book,
        kor = R.string.category_book_kor,
    ),
    STUDY(
        iconRes = R.drawable.ic_memo,
        kor = R.string.category_study_kor,
    ),
    WORK(
        iconRes = R.drawable.ic_monitor,
        kor = R.string.category_work_kor,
    ),
    ;

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
