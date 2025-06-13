package com.data.network.endpoint

object Endpoint {
    object Host {
        const val baseURL = "http://60.196.157.199:8080"
    }

    object Path {
        const val SEARCH = "/api/companies/search"
    }

    object Query {
        const val KEYWORD       = "keyword"
        const val LATITUDE      = "latitude"
        const val LONGITUDE     = "longitude"
        const val SIZE          = "size"
        const val PAGE          = "page"
    }
}