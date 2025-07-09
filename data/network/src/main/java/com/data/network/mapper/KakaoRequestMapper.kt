package com.data.network.mapper

import RequestModel.ReverseGeocodingRequestModel
import com.data.network.endpoint.KakaoMapEndpoint
import javax.inject.Inject

class KakaoRequestMapper @Inject constructor() {
    fun toDTO(requestModel: ReverseGeocodingRequestModel): Map<String, Any> = mapOf(
        KakaoMapEndpoint.Query.X to requestModel.x,
        KakaoMapEndpoint.Query.Y to requestModel.y
    )
}