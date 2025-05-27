package com.data.network.endpoint

object Endpoint {
    object Host {
        const val baseURL           = "https://api.themoviedb.org/3/"
    }

    object Path {
        const val SEARCH_MOVIE      = "3/search/movie"
    }

    object Query {
        const val api_key       = "api_key"
        const val query         = "query"
    }
}