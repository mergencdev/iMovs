package com.mergenc.imovs.utils.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.api.TMDbApiInterface
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesFactory(
    private val apiService: TMDbApiInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, PopularMovie>() {

    val popularMoviesLiveDataSource = MutableLiveData<PopularMoviesDataSource>()
    override fun create(): DataSource<Int, PopularMovie> {
        val popularMoviesDataSource = PopularMoviesDataSource(apiService, compositeDisposable)
        popularMoviesLiveDataSource.postValue(popularMoviesDataSource)
        return popularMoviesDataSource
    }


}