package com.pomonyang.mohanyang.presentation.model.cat

import androidx.compose.runtime.Stable
import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse

@Stable
data class CatInfoModel(
    val no: Int,
    val name: String,
    val type: CatType
)

fun CatTypeResponse.toModel(): CatInfoModel = CatInfoModel(
    no = no,
    name = name,
    type = CatType.safeValueOf(type)
)
