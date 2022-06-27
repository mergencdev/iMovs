package com.mergenc.imovs.utils.api

import com.mergenc.imovs.model.MovieDetails
import com.mergenc.imovs.model.PopularMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiInterface {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<PopularMoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}