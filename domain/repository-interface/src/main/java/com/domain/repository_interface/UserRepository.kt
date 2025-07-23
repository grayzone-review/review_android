package com.domain.repository_interface

import com.domain.entity.InteractionCounts
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.User

interface UserRepository {
    suspend fun userInfo(): User
    suspend fun myInteractionCounts(): InteractionCounts
    suspend fun myReviews(): MyArchiveReviews
    suspend fun myInterestReviews(): MyArchiveReviews
    suspend fun myFollowCompanies(): MyArchiveCompanies
}