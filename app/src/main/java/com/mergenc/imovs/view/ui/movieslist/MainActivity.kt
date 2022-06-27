package com.mergenc.imovs.view.ui.movieslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mergenc.imovs.databinding.ActivityMainBinding
import com.mergenc.imovs.utils.api.TMDbApiInterface
import com.mergenc.imovs.utils.api.TMDbClient
import com.mergenc.imovs.utils.repository.Network
import com.mergenc.imovs.view.ui.moviedetails.MovieDetailsActivity
import com.mergenc.imovs.view.ui.moviedetails.MovieDetailsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MoviesListViewModel
    lateinit var popularMovieRepository: PopularMoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val apiService: TMDbApiInterface = TMDbClient.getClient()
        popularMovieRepository = PopularMoviesPagedListRepository(apiService)

        viewModel = getViewModel()

        val popularMoviesAdapter = PopularMoviesAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = popularMoviesAdapter.getItemViewType(position)
                return if (viewType == popularMoviesAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        binding.rvPopularMovies.layoutManager = gridLayoutManager
        binding.rvPopularMovies.setHasFixedSize(true)
        binding.rvPopularMovies.adapter = popularMoviesAdapter

        viewModel.popularMoviesPagedList.observe(this, Observer {
            popularMoviesAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility =
                if (viewModel.listIsEmpty() && it == Network.LOADING) View.VISIBLE else View.GONE
            binding.tvConnectionErrorPopular.visibility =
                if (viewModel.listIsEmpty() && it == Network.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()) {
                popularMoviesAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MoviesListViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MoviesListViewModel(popularMovieRepository) as T
            }
        })[MoviesListViewModel::class.java]

    }
}