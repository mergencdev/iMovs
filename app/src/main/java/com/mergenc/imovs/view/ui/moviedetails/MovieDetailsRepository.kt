package com.mergenc.imovs.view.ui.moviedetails

import androidx.lifecycle.LiveData
import com.mergenc.imovs.model.MovieDetails
import com.mergenc.imovs.utils.api.TMDbApiInterface
import com.mergenc.imovs.utils.repository.MovieDetailsNetworkDataSource
import com.mergenc.imovs.utils.repository.Network
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TMDbApiInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<Network> {
        return movieDetailsNetworkDataSource.networkState
    }

}