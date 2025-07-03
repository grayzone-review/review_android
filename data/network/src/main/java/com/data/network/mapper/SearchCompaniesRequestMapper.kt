package com.data.network.mapper

import RequestModel.SearchCompaniesRequestModel
import com.data.network.endpoint.UpEndpoint
import javax.inject.Inject

class SearchCompaniesRequestMapper @Inject constructor() {
    fun toDTO(requestModel: SearchCompaniesRequestModel): Map<String, Any> = mapOf(
        UpEndpoint.Query.KEYWORD   to requestModel.keyword,
        UpEndpoint.Query.LATITUDE  to requestModel.latitude,
        UpEndpoint.Query.LONGITUDE to requestModel.longitude,
        UpEndpoint.Query.SIZE      to requestModel.size,
        UpEndpoint.Query.PAGE      to requestModel.page
    )
} 