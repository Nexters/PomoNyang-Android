package com.pomonyang.mohanyang.domain.model.cat

// TODO push 및 타입 등 지정 or API
enum class CatType(val pushContent: String) {
    CHEESE("어디갔냐옹..."),
    BLACK("어디갔냐옹..."),
    THREE_COLOR("내가 여기있는데 어디갔냐옹!");

    companion object {
        fun safeValueOf(type: String, default: CatType = CHEESE): CatType = try {
            CatType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
