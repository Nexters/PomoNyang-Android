package com.pomonyang.mohanyang.domain.model.cat

import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse

data class CatSelectionContent(
    val no: Int,
    val name: String,
    val type: CatType
)

fun CatTypeResponse.toModel(): CatSelectionContent = CatSelectionContent(
    no = this.no,
    name = this.name,
    type = CatType.safeValueOf(this.type)
)
