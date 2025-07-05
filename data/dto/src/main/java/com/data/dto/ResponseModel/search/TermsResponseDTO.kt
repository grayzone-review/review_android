package com.data.dto.ResponseModel.search

import com.domain.entity.TermInfo
import com.domain.entity.Terms

data class TermsResponseDTO(
    val terms: List<TermDTO>,
)

data class TermDTO(
    val term: String,
    val url: String,
    val code: String,
    val required: Boolean,
)

fun TermsResponseDTO.toDomain(): Terms {
    return Terms(terms.map { it.toDomain() })
}

private fun TermDTO.toDomain(): TermInfo {
    return TermInfo(term, url, code, required)
}