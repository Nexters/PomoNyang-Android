package com.pomonyang.mohanyang.data.remote.util

import com.pomonyang.mohanyang.data.remote.model.response.ErrorResponse

class InternalException(errorResponse: ErrorResponse) : Exception(errorResponse.message)
class BadRequestException(errorResponse: ErrorResponse) : Exception(errorResponse.message)
class ForbiddenException(errorResponse: ErrorResponse) : Exception(errorResponse.message)
