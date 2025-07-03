package com.data.network.mapper

import RequestModel.ReverseGeocodingRequestModel
import com.data.network.endpoint.KakaoMapEndpoint

class KakaoMapRequestMapper {
    fun toDTO(requestModel: ReverseGeocodingRequestModel): Map<String, Any> = mapOf(
        KakaoMapEndpoint.Query.X to requestModel.x,
        KakaoMapEndpoint.Query.Y to requestModel.y
    )
}