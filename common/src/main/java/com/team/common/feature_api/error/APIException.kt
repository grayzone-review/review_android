package com.team.common.feature_api.error

class APIException(
    val action: ErrorAction,
    override val message: String
) : RuntimeException(message)