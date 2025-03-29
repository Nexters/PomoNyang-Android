package com.pomonyang.mohanyang.presentation.screen.home.category.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
enum class CategoryIcon(
    @DrawableRes val resourceId: Int,
) {
    CAT(resourceId = com.mohanyang.presentation.R.drawable.ic_category_default),
    BOX_PEN(resourceId = com.mohanyang.presentation.R.drawable.ic_box_pen),
    OPEN_BOOK(resourceId = com.mohanyang.presentation.R.drawable.ic_open_book),
    BRIFECASE(resourceId = com.mohanyang.presentation.R.drawable.ic_brifecase),
    LAPTOP(resourceId = com.mohanyang.presentation.R.drawable.ic_monitor),
    DUMBBELL(resourceId = com.mohanyang.presentation.R.drawable.ic_dumbbell),
    LIGHTNING(resourceId = com.mohanyang.presentation.R.drawable.ic_lightning),
    FIRE(resourceId = com.mohanyang.presentation.R.drawable.ic_fire),
    HEART(resourceId = com.mohanyang.presentation.R.drawable.ic_heart),
    ASTERISK(resourceId = com.mohanyang.presentation.R.drawable.ic_asterisk),
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
}
