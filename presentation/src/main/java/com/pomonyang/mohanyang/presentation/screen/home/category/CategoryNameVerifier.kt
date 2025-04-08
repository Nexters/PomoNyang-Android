package com.pomonyang.mohanyang.presentation.screen.home.category

import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.onboarding.naming.ValidationResult
import kotlinx.collections.immutable.ImmutableList

object CategoryNameVerifier {

    private const val NAME_MAX_LENGTH = 10
    private const val NAME_MIN_LENGTH = 1

    fun validateCategoryName(name: String, categoryList: ImmutableList<PomodoroCategoryModel>): ValidationResult = when {
        categoryList.any { it.title == name } -> ValidationResult(false, "이미 존재하는 카테고리예요.")
        else -> ValidationResult(
            isValid = name.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH,
            message = if (name.length <= NAME_MAX_LENGTH) "" else "최대 ${NAME_MAX_LENGTH}자리까지 입력할 수 있어요.",
        )
    }
}
