package com.domain.repository_interface

import com.domain.entity.InteractionCounts
import com.domain.entity.ModifyUserInfoResult
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.ReportResult
import com.domain.entity.ResignResult
import com.domain.entity.User

interface UserRepository {
    suspend fun userInfo(): User?
    suspend fun modifyUserInfo(mainRegionID: Int, interestedRegionIds: List<Int>, nickname: String): ModifyUserInfoResult
    suspend fun resign(refreshToken: String): ResignResult
    suspend fun report(reporterName: String, targetName: String, reportType: String, description: String): ReportResult
    suspend fun myInteractionCounts(): InteractionCounts
    suspend fun myReviews(): MyArchiveReviews
    suspend fun myInterestReviews(): MyArchiveReviews
    suspend fun myFollowCompanies(): MyArchiveCompanies
}