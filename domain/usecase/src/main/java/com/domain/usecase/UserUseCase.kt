package com.domain.usecase

import com.domain.entity.InteractionCounts
import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveReviews
import com.domain.entity.User
import com.domain.repository_interface.UserRepository
import javax.inject.Inject

interface UserUseCase {
    suspend fun userInfo(): User
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