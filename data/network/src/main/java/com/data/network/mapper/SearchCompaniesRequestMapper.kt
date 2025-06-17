package com.data.network.mapper

import RequestModel.SearchCompaniesRequestModel
import com.data.network.endpoint.Endpoint
import javax.inject.Inject

class SearchCompaniesRequestMapper @Inject constructor() {
    fun toDTO(requestModel: SearchCompaniesRequestModel): Map<String, Any> = mapOf(
        Endpoint.Query.KEYWORD   to requestModel.keyword,
        Endpoint.Query.LATITUDE  to requestModel.latitude,
        Endpoint.Query.LONGITUDE to requestModel.longitude,
        Endpoint.Query.SIZE      to requestModel.size,
        Endpoint.Query.PAGE      to requestModel.page
    )
} 