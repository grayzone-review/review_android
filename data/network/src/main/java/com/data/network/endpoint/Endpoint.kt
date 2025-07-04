package com.data.network.endpoint

object Endpoint {
    object Host {
        //60.196.157.199
        const val baseURL = "http://10.0.2.2:8080"
//        const val baseURL = "http://60.196.157.199:8080"
    }

    object Path {
        const val SEARCH = "/api/companies/search"
        const val COMPANY_INFO = "/api/companies/{companyID}"
    }

    object Query {
        const val KEYWORD       = "keyword"
        const val LATITUDE      = "latitude"
        const val LONGITUDE     = "longitude"
        const val SIZE          = "size"
        const val PAGE          = "page"
    }
}