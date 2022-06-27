package com.mergenc.imovs.utils.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mergenc.imovs.model.MovieDetails
import com.mergenc.imovs.utils.api.TMDbApiInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService: TMDbApiInterface,
    private val compositeDisposable
    : CompositeDisposable
) {
    private val _networkState = MutableLiveData<Network>()
    val networkState: LiveData<Network>
        get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(Network.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(Network.LOADED)
                        },
                        {
                            _networkState.postValue(Network.ERROR)
                        }
                    )
            )
        } catch (e: Exception) {
            //_networkState.postValue(Network.ERROR)
            e.printStackTrace()
        }
    }
}