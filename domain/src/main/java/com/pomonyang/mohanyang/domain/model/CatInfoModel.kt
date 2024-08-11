package com.pomonyang.mohanyang.domain.model

import com.pomonyang.mohanyang.data.remote.model.response.CatInfoResponse
import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse

data class CatInfoModel(
    val no: Int,
    val catTypeResponse: CatTypeModel,
    val name: String
)

enum class CatTypeModel {
    CHEESE,
    BLACK,
    THREE_COLOR
}

fun CatInfoResponse.toModel() = CatInfoModel(
    no = no,
    catTypeResponse = catType.toModel(),
    name = name
)

fun CatTypeResponse.toModel(): CatTypeModel = when (this) {
    CatTypeResponse.CHEESE -> CatTypeModel.CHEESE
    CatTypeResponse.BLACK -> CatTypeModel.BLACK
    CatTypeResponse.THREE_COLOR -> CatTypeModel.THREE_COLOR
}
