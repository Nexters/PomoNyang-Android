package com.pomonyang.mohanyang.data.remote.util

class InternalException(code: Int = 500, msg: String) : Exception("$code: $msg")
class BadRequestException(code: Int = 400, msg: String) : Exception("$code: $msg")
class ForbiddenException(code: Int = 401, msg: String) : Exception("$code: $msg")
