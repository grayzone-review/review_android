package com.data.network.endpoint

object Endpoint {
    object Host {
        const val baseURL = "http://localhost:8080"
    }

    object Path {
        const val SEARCH = "/api/companies/search"
    }

    object Query {
        const val KEYWORD       = "api_key"
        const val LATITUDE      = "query"
        const val LONGITUDE     = "longitude"
        const val SIZE          = "size"
        const val PAGE          = "page"
    }
}