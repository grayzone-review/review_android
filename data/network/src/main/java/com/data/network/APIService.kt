package com.data.network

import com.data.network.endpoint.Endpoint
import com.data.network.response.APIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(Endpoint.Path.SEARCH_MOVIE)
    suspend fun getTestData(
        @Query(Endpoint.Query.api_key) apikey: String,
        @Query(Endpoint.Query.query) keyword: String
    ): APIResponse<String>
}