package com.pomonyang.mohanyang.presentation.screen.home.category.model

import androidx.annotation.StringRes
import com.mohanyang.presentation.R

enum class CategoryManageState(
    @StringRes val title: Int,
) {
    DEFAULT(R.string.change_category_title),
    EDIT(R.string.change_category_edit_title),
    DELETE(R.string.change_category_delete_title),
    ;

    fun isEdit() = this == EDIT

    fun isDefault() = this == DEFAULT
}
