package com.mergenc.imovs.view.ui.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.repository.Network
import io.reactivex.disposables.CompositeDisposable

class MoviesListViewModel(private val popularMoviesRepository: PopularMoviesPagedListRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularMoviesPagedList: LiveData<PagedList<PopularMovie>> by lazy {
        popularMoviesRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<Network> by lazy {
        popularMoviesRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return popularMoviesPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}