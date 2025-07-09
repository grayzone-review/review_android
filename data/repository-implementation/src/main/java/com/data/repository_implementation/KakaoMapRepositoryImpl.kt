package com.data.repository_implementation

import RequestModel.ReverseGeocodingRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.KakaoMapAPIService
import com.data.network.mapper.KakaoRequestMapper
import com.domain.entity.ReverseGeocodingRegions
import com.domain.repository_interface.KakaoMapRepository
import javax.inject.Inject

class KakaoMapRepositoryImpl @Inject constructor(
    private val kakaoMapAPIService: KakaoMapAPIService,
    private val kakaoMapRequestMapper: KakaoRequestMapper
): KakaoMapRepository {
    override suspend fun reverseGeocoding(
        xLongitude: Double,
        yLatitude: Double
    ): ReverseGeocodingRegions {
        val requestModel = ReverseGeocodingRequestModel(x = xLongitude, y = yLatitude)
        val requestDTO = kakaoMapRequestMapper.toDTO(requestModel)
        val responseDTO = kakaoMapAPIService.reverseGeocoding(requestDTO)
        return responseDTO.toDomain()
    }
}