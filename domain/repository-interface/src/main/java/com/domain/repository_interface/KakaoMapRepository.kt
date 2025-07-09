package com.domain.repository_interface

import com.domain.entity.ReverseGeocodingRegions

interface KakaoMapRepository {
    suspend fun reverseGeocoding(
        xLongitude: Double,
        yLatitude: Double
    ): ReverseGeocodingRegions
}