package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import javax.inject.Inject

data class ValidationResult(val isValid: Boolean, val message: String = "")

class CatNameVerifier @Inject constructor() {

    fun verifyName(name: String): ValidationResult {
        val validators = listOf(
            ::validateLength,
            ::validatePattern,
            ::validateFirstSpace
        )

        return validators.asSequence()
            .map { it(name) }
            .firstOrNull { it.isValid.not() }
            ?: ValidationResult(true)
    }

    private fun validateLength(name: String) = ValidationResult(
        isValid = name.length <= NAME_MAX_LENGTH,
        message = if (name.length <= NAME_MAX_LENGTH) "" else "최대 ${NAME_MAX_LENGTH}자리까지 허용됩니다."
    )

    private fun validatePattern(name: String) = ValidationResult(
        isValid = NAME_PATTERN.toRegex().matches(name),
        message = if (NAME_PATTERN.toRegex().matches(name)) "" else "특수 문자는 사용할 수 없습니다."
    )

    private fun validateFirstSpace(name: String) = ValidationResult(
        isValid = !name.first().isWhitespace(),
        message = if (name.first().isWhitespace()) "고양이 이름은 빈 칸이 될 수 없어요" else ""
    )

    companion object {
        private const val NAME_MAX_LENGTH = 10
        private const val NAME_MIN_LENGTH = 1
        private const val NAME_PATTERN = "^[\\w\\s\\n]{${NAME_MIN_LENGTH},${NAME_MAX_LENGTH}}$"
    }
}
