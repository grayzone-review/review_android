package com.domain.entity

data class ReverseGeocodingRegions(
    val regions: List<Region>
)

data class Region(
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
