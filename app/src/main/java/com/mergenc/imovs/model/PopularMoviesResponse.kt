package com.mergenc.imovs.model


import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val popularMoviesList: List<PopularMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)