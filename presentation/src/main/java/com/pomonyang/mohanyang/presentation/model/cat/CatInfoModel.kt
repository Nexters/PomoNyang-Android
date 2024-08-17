package com.pomonyang.mohanyang.presentation.model.cat

import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse

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
