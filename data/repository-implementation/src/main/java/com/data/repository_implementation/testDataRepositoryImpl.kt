package com.data.repository_implementation

import com.data.network.APIService
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    /** 영화 검색 → API가 돌려주는 raw JSON(String) */
    suspend fun searchMovie(keyword: String): String
}

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: APIService
) : MovieRepository {


    override suspend fun searchMovie(keyword: String): String =
        api.getTestData(
            apikey  = "58d809cdf82571289dc2cbd7a96c0343",
            keyword = keyword
        )
}