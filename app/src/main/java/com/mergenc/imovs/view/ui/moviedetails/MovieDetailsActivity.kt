package com.mergenc.imovs.view.ui.moviedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mergenc.imovs.databinding.ActivityMovieDetailsBinding
import com.mergenc.imovs.model.Cast.Cast
import com.mergenc.imovs.model.Cast.CastResponse
import com.mergenc.imovs.model.MovieDetails
import com.mergenc.imovs.utils.api.POSTER_BASE_URL
import com.mergenc.imovs.utils.api.TMDbApiInterface
import com.mergenc.imovs.utils.api.TMDbClient
import com.mergenc.imovs.utils.repository.Network
import com.mergenc.imovs.view.ui.cast.CastRecyclerViewAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    lateinit var postList: MutableList<Cast>
    var homepage: String = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(TMDbApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TMDbApiInterface = TMDbClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {

            bindUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            if (it == Network.LOADING) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

            if (it == Network.ERROR) {
                binding.materialCardViewError.visibility = View.VISIBLE
            } else {
                binding.materialCardViewError.visibility = View.GONE
            }
        })


        apiService.getCast(movieId).enqueue(object : retrofit2.Callback<CastResponse> {
            override fun onFailure(call: retrofit2.Call<CastResponse>, t: Throwable) {
                println("failure")
            }

            override fun onResponse(
                call: retrofit2.Call<CastResponse>,
                response: retrofit2.Response<CastResponse>
            ) {

                binding.rvCast.layoutManager = LinearLayoutManager(
                    this@MovieDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val adapter =
                    CastRecyclerViewAdapter(response.body()!!.cast, this@MovieDetailsActivity)
                binding.rvCast.adapter = adapter
            }
        })
    }

    fun bindUi(it: MovieDetails) {
        val movieYear = it.releaseDate.substring(0, 4)
        val imdbRating = it.rating.toString().substring(0, 3) + "/10"

        binding.tvMovieTitle.text = it.title
        binding.tvMovieYear.text = movieYear
        binding.tvMovieRuntime.text = it.runtime.toString() + " min"
        binding.tvImdbRating.text = imdbRating
        binding.tvMovieDescription.text = it.overview
        binding.tvDot.text = " • "
        binding.tvMovieCast.text = "Cast"
        homepage = it.homepage

        val movieBackdropUrl: String = POSTER_BASE_URL + it.backdropPath
        Glide.with(this).load(movieBackdropUrl).centerCrop().into(binding.ivBackdropPath)

    }

    private fun getViewModel(movieId: Int): MovieDetailsViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailsViewModel(movieDetailsRepository, movieId) as T
            }
        })[MovieDetailsViewModel::class.java]

    }
}