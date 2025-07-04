package com.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LegalDistrictInfo(
    val id: Long,
    val name: String
): Parcelable

data class LegalDistrictSearchResult(
    val hasNext: Boolean,
    val currentPage: Int,
    val districts: List<LegalDistrictInfo>
)