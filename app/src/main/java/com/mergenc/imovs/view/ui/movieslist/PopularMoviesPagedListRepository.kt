package com.mergenc.imovs.view.ui.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.api.POST_PER_PAGE
import com.mergenc.imovs.utils.api.TMDbApiInterface
import com.mergenc.imovs.utils.repository.Network
import com.mergenc.imovs.utils.repository.PopularMoviesDataSource
import com.mergenc.imovs.utils.repository.PopularMoviesFactory
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesPagedListRepository(private val apiService: TMDbApiInterface) {
    lateinit var popularMoviesPagedList: LiveData<PagedList<PopularMovie>>
    lateinit var popularMoviesDataSourceFactory: PopularMoviesFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PopularMovie>> {
        popularMoviesDataSourceFactory = PopularMoviesFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        popularMoviesPagedList =
            LivePagedListBuilder(popularMoviesDataSourceFactory, config).build()
        return popularMoviesPagedList
    }

    fun getNetworkState(): LiveData<Network> {
        return Transformations.switchMap<PopularMoviesDataSource, Network>(
            popularMoviesDataSourceFactory.popularMoviesLiveDataSource,
            PopularMoviesDataSource::network
        )
    }
}