package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.domain.entity.InteractionCounts
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.User
import com.domain.repository_interface.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): UserRepository {
    override suspend fun userInfo(): User {
        val responseDTO = upAPIService.userInfo()
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun myInteractionCounts(): InteractionCounts {
        val responseDTO = upAPIService.myInteractionCounts()
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun myReviews(): MyArchiveReviews {
        val responseDTO = upAPIService.myReviews()
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun myInterestReviews(): MyArchiveReviews {
        val responseDTO = upAPIService.myInterestReviews()
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun myFollowCompanies(): MyArchiveCompanies {
        val responseDTO = upAPIService.myFollowCompanies()
        return responseDTO.data?.toDomain()!!
    }

}