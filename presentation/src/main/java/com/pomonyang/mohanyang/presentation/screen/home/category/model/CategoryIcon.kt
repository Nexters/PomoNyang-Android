package com.pomonyang.mohanyang.presentation.screen.home.category.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
enum class CategoryIcon(
    @DrawableRes val resourceId: Int,
) {
    CAT(resourceId = com.mohanyang.presentation.R.drawable.ic_category_default),
    MEMO(resourceId = com.mohanyang.presentation.R.drawable.ic_memo),
    BOOK(resourceId = com.mohanyang.presentation.R.drawable.ic_book),
    BRIFECASE(resourceId = com.mohanyang.presentation.R.drawable.ic_brifecase),
    LAPTOP(resourceId = com.mohanyang.presentation.R.drawable.ic_laptop),
    EXERCISE(resourceId = com.mohanyang.presentation.R.drawable.ic_exercise),
    REST(resourceId = com.mohanyang.presentation.R.drawable.ic_rest),
    FOCUS(resourceId = com.mohanyang.presentation.R.drawable.ic_focus),
    HEART(resourceId = com.mohanyang.presentation.R.drawable.ic_heart),
    STAR(resourceId = com.mohanyang.presentation.R.drawable.ic_star),
    SUN(resourceId = com.mohanyang.presentation.R.drawable.ic_sun),
    MOON(resourceId = com.mohanyang.presentation.R.drawable.ic_moon),

    ;

    object CategoryIconNavType : androidx.navigation.NavType<CategoryIcon>(isNullableAllowed = false) {
        override fun get(bundle: android.os.Bundle, key: String): CategoryIcon? {
            return bundle.getString(key)?.let { id ->
                CategoryIcon.valueOf(id)
            }
        }

        override fun parseValue(value: String): CategoryIcon {
            Timber.tag("koni").d("value > $value")
            return CategoryIcon.valueOf(value)
        }

        override fun put(bundle: android.os.Bundle, key: String, value: CategoryIcon) {
            bundle.putString(key, value.name)
        }
    }

    companion object {

        fun safeValueOf(title: String): CategoryIcon {
            // TODO 추후에 서버에서 Icon까지 내려주면 이렇게 바꿀 필요가 없음
            return when (title.lowercase()) {
                "독서" -> BOOK
                "공부" -> MEMO
                "작업" -> LAPTOP
                else -> CAT
            }
        }
    }
}
