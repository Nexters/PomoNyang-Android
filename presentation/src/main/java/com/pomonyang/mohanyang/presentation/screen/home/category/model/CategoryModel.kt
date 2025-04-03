package com.pomonyang.mohanyang.presentation.screen.home.category.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val name: String,
    val icon: CategoryIcon = CategoryIcon.CAT,
) : java.io.Serializable
