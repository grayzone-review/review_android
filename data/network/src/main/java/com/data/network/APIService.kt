package com.data.network

import com.data.network.endpoint.Endpoint
import com.data.network.response.APIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(Endpoint.Path.SEARCH)
    suspend fun searchCompanies(
        @Query(Endpoint.Query.KEYWORD) apikey: String,
        @Query(Endpoint.Query.LATITUDE) keyword: Double,
        @Query(Endpoint.Query.LONGITUDE) longitude: Double,
        @Query(Endpoint.Query.SIZE) size: Int,
        @Query(Endpoint.Query.PAGE) page: Int
    ): APIResponse<String>
}