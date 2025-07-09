package com.data.network.endpoint

object KakaoMapEndpoint {

    object Host {
        const val baseURL = "https://dapi.kakao.com"
    }

    object Path {
        const val COORDINATOR2REGION = "/v2/local/geo/coord2regioncode.json"
    }

    object Query {
        /** 경도 (x) - Longitude */
        const val X = "x"
        /** 위도 (y) - Latitude */
        const val Y = "y"
    }
}