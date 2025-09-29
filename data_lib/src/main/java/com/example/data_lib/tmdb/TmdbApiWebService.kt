package com.example.data_lib.tmdb

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiWebService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): String
}