package com.data.network.endpoint

object UpEndpoint {
    object Host {
        //60.196.157.199
//        const val baseURL = "http://10.0.2.2:8080"
//        const val baseURL = "https://3.35.24.214.sslip.io/"
//        const val baseURL = "http://60.196.157.199:8080"
        const val baseURL = "https://3.35.24.214.sslip.io"
    }

    object Path {
        const val SEARCH = "/api/companies/search"
        const val COMPANY_INFO = "/api/companies/{companyId}"
        const val COMPANY_REVIEW = "/api/companies/{companyId}/reviews"
        const val SEARCH_LEGAL_DISTRICTS = "/api/legal-districts"
        const val AUTH_LOGIN = "/api/auth/login"
        const val AUTH_SIGNUP = "/api/auth/signup"
        const val AUTH_LOGOUT = "/api/auth/logout"
        const val NICKNAME_VERIFY = "/api/users/nickname-verify"
        const val TERM = "/api/auth/terms"
        const val POPULAR_REVIEWS = "/api/reviews/popular"
        const val MY_TOWN_REVIEWS = "/api/reviews/main-region"
        const val INTEREST_REGIONS_REVIEWS = "/api/reviews/interested-region"
        const val FOLLOW_COMPANY = "/api/companies/{companyId}/follows"
        const val LIKE_REVIEW = "/api/reviews/{reviewId}/likes"
        const val REVIEW_COMMENT = "/api/reviews/{reviewId}/comments"
        const val COMMENT_REPLIES = "/api/comments/{commentId}/replies"
        const val MY_INTERACTION_COUNT = "/api/users/me/interaction-counts"
        const val MY_INFO = "/api/users/me"
        const val MY_REVIEWS = "/api/users/me/reviews"
        const val MY_INTEREST_REVIEWS = "/api/users/me/interacted-reviews"
        const val MY_FOLLOW = "/api/users/me/followed-companies"
        const val NEARBY_COMPANY = "/api/companies/nearby"
        const val MAIN_REGION_COMPANY = "/api/companies/main-region"
        const val INTEREST_REGIONS_COMPANY = "/api/companies/interested-region"

        const val ACCOUNT_RESIGN = "/api/users/me"
        const val REPORT = "/api/reports"
    }

    object Query {
        const val KEYWORD       = "keyword"
        const val LATITUDE      = "latitude"
        const val LONGITUDE     = "longitude"
        const val SIZE          = "size"
        const val PAGE          = "page"
        const val COMPANY_ID    = "companyId"
        const val REVIEW_ID     = "reviewId"
        const val COMMENT_ID    = "commentId"
    }
}