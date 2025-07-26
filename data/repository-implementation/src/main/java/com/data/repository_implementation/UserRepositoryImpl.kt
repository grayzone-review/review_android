package com.data.repository_implementation

import RequestModel.ModifyUserRequestModel
import RequestModel.ReportRequestModel
import RequestModel.ResignRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.domain.entity.InteractionCounts
import com.domain.entity.ModifyUserInfoResult
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.ReportResult
import com.domain.entity.ResignResult
import com.domain.entity.User
import com.domain.repository_interface.UserRepository
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.toErrorAction
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): UserRepository {
    override suspend fun userInfo(): User {
        val responseDTO = upAPIService.userInfo()
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun modifyUserInfo(
        mainRegionID: Int,
        interestedRegionIds: List<Int>,
        nickname: String
    ): ModifyUserInfoResult {
        val requestDTO = ModifyUserRequestModel(mainRegionId = mainRegionID, interestedRegionIds = interestedRegionIds, nickname = nickname)
        val responseDTO = upAPIService.modifyUserInfo(requestModel = requestDTO)
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return ModifyUserInfoResult(success = responseDTO.success, message = responseDTO.message)
    }

    override suspend fun resign(
        refreshToken: String
    ): ResignResult {
        val requestDTO = ResignRequestModel(refreshToken = refreshToken)
        val responseDTO = upAPIService.resign(requestModel = requestDTO)
        return ResignResult(success = responseDTO.success, message = responseDTO.message)
    }

    override suspend fun report(
        reporterName: String,
        targetName: String,
        reportType: String,
        description: String
    ): ReportResult {
        val requestDTO = ReportRequestModel(reporterName = reporterName, targetName = targetName, reportType = reportType, description = description)
        val responseDTO = upAPIService.report(requestModel = requestDTO)
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return ReportResult(success = responseDTO.success, message = responseDTO.message)
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