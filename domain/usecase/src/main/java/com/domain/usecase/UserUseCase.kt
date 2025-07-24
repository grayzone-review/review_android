package com.domain.usecase

import com.domain.entity.InteractionCounts
import com.domain.entity.ModifyUserInfoResult
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.ReportResult
import com.domain.entity.ResignResult
import com.domain.entity.User
import com.domain.repository_interface.UserRepository
import javax.inject.Inject

interface UserUseCase {
    suspend fun userInfo(): User
    suspend fun modifyUserInfo(mainRegionID: Int, interestedRegionIds: List<Int>, nickname: String): ModifyUserInfoResult
    suspend fun resign(refreshToken: String): ResignResult
    suspend fun report(reporterName: String, targetName: String, reportType: String, description: String): ReportResult
    suspend fun myInteractionCounts(): InteractionCounts
    suspend fun myReviews(): MyArchiveReviews
    suspend fun myInterestReviews(): MyArchiveReviews
    suspend fun myFollowCompanies(): MyArchiveCompanies
}

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): UserUseCase {
    override suspend fun userInfo(): User {
        return userRepository.userInfo()
    }

    override suspend fun modifyUserInfo(mainRegionID: Int, interestedRegionIds: List<Int>, nickname: String, ): ModifyUserInfoResult {
        return userRepository.modifyUserInfo(mainRegionID, interestedRegionIds, nickname)
    }

    override suspend fun resign(refreshToken: String): ResignResult {
        return userRepository.resign(refreshToken)
    }

    override suspend fun report(reporterName: String, targetName: String, reportType: String, description: String, ): ReportResult {
        return userRepository.report(reporterName, targetName, reportType, description)
    }

    override suspend fun myInteractionCounts(): InteractionCounts {
        return userRepository.myInteractionCounts()
    }

    override suspend fun myReviews(): MyArchiveReviews {
        return userRepository.myReviews()
    }

    override suspend fun myInterestReviews(): MyArchiveReviews {
        return userRepository.myInterestReviews()
    }

    override suspend fun myFollowCompanies(): MyArchiveCompanies {
        return userRepository.myFollowCompanies()
    }
}