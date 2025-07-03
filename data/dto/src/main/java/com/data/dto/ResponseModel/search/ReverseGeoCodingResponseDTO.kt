package com.data.dto.ResponseModel.search

import com.domain.entity.Region
import com.domain.entity.ReverseGeocodingRegions

data class ReverseGeoCodingResponseDTO(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    val total_count: Int
)

data class Document(
    val region_type: String,
    val code: String,
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val region_4depth_name: String,
    val x: Double,
    val y: Double
)

private fun Document.toDomain(): Region =
    Region(
        region_type,
        code,
        address_name,
        region_1depth_name,
        region_2depth_name,
        region_3depth_name,
        region_4depth_name,
        x,
        y
    )

fun ReverseGeoCodingResponseDTO.toDomain(): ReverseGeocodingRegions =
    ReverseGeocodingRegions(
        regions = documents.map(Document::toDomain)
    )