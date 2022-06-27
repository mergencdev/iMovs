package com.mergenc.imovs.utils.repository

import android.graphics.Movie
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.api.FIRST_PAGE
import com.mergenc.imovs.utils.api.TMDbApiInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularMoviesDataSource(
    private val apiService: TMDbApiInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, PopularMovie>() {

    private var page = FIRST_PAGE
    val network: MutableLiveData<Network> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovie>
    ) {
        network.postValue(Network.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.popularMoviesList, null, page + 1)
                        network.postValue(Network.LOADED)
                    },
                    {
                        network.postValue(Network.ERROR)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {
        network.postValue(Network.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.popularMoviesList, params.key + 1)
                            network.postValue(Network.LOADED)
                        } else {
                            network.postValue(Network.END)
                        }
                    },
                    {
                        network.postValue(Network.ERROR)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {

    }
}