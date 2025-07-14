package com.data.network.endpoint

object UpEndpoint {
    object Host {
        //60.196.157.199
        const val baseURL = "http://10.0.2.2:8080"
//        const val baseURL = "http://60.196.157.199:8080"
    }

    object Path {
        const val SEARCH = "/api/companies/search"
        const val COMPANY_INFO = "/api/companies/{companyID}"
        const val SEARCH_LEGAL_DISTRICTS = "/api/legal-districts"
        const val AUTH_LOGIN = "/api/auth/login"
        const val AUTH_SIGNUP = "/api/auth/signup"
        const val NICKNAME_VERIFY = "/api/users/nickname-verify"
        const val TERM = "/api/auth/terms"
    }

    object Query {
        const val KEYWORD       = "keyword"
        const val LATITUDE      = "latitude"
        const val LONGITUDE     = "longitude"
        const val SIZE          = "size"
        const val PAGE          = "page"
    }
}