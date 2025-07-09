package com.data.network.api_service

import com.data.dto.ResponseModel.search.ReverseGeoCodingResponseDTO
import com.data.network.endpoint.KakaoMapEndpoint
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface KakaoMapAPIService {
    @GET(KakaoMapEndpoint.Path.COORDINATOR2REGION)
    suspend fun reverseGeocoding(
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): ReverseGeoCodingResponseDTO
}