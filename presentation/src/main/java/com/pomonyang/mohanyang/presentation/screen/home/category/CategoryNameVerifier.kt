package com.pomonyang.mohanyang.presentation.screen.home.category

import com.pomonyang.mohanyang.presentation.screen.onboarding.naming.ValidationResult

object CategoryNameVerifier {

    private const val NAME_MAX_LENGTH = 10
    private const val NAME_MIN_LENGTH = 1

    fun validateCategoryName(name: String): ValidationResult {
        // TODO 기존 카테고리 이름 리스트 가져와서 중복값 확인하는 로직 추가
        return ValidationResult(
            isValid = name.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH,
            message = if (name.length <= NAME_MAX_LENGTH) "" else "최대 ${NAME_MAX_LENGTH}자리까지 입력할 수 있어요.",
        )
    }
}
