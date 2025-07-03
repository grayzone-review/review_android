package com.domain.usecase

import com.domain.entity.ReverseGeocodingRegions
import com.domain.repository_interface.KakaoMapRepository
import javax.inject.Inject

interface KakaoMapUseCase {
    suspend fun reverseGeocoding(
        xLongitude: Double,
        yLatitude: Double
    ): ReverseGeocodingRegions
}

class KakaoMapUseCaseImpl @Inject constructor(
    private val kakaoMapRepository: KakaoMapRepository
) : KakaoMapUseCase {
    override suspend fun reverseGeocoding(
        xLongitude: Double,
        yLatitude: Double
    ): ReverseGeocodingRegions {
        return kakaoMapRepository.reverseGeocoding(xLongitude, yLatitude)
    }
}