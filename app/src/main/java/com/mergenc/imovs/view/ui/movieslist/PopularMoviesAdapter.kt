package com.mergenc.imovs.view.ui.movieslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.api.POSTER_BASE_URL
import com.mergenc.imovs.utils.repository.Network
import com.mergenc.imovs.view.ui.moviedetails.MovieDetailsActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_movie.view.*

class PopularMoviesAdapter(val context: Context) :
    PagedListAdapter<PopularMovie, RecyclerView.ViewHolder>(PopularMovieDiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: Network? = null


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as PopularMoviesAdapter.PopularMovieItemViewHolder).bind(
                getItem(position),
                context
            )
        } else {
            (holder as PopularMoviesAdapter.NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != Network.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(com.mergenc.imovs.R.layout.item_movie, parent, false)
            return PopularMovieItemViewHolder(view)
        } else {
            view =
                layoutInflater.inflate(com.mergenc.imovs.R.layout.item_network_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    class PopularMovieDiffCallback : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem == newItem
        }

    }

    class PopularMovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(popularMovie: PopularMovie?, context: Context) {
            itemView.tvMovieName.text = popularMovie?.title
            itemView.tvMovieYear.text = popularMovie?.releaseDate

            val moviePosterUrl: String = POSTER_BASE_URL + popularMovie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(itemView.ivMoviePoster)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", popularMovie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: Network?) {
            if (networkState != null && networkState == Network.LOADING) {
                itemView.progressBarPopular?.visibility = View.VISIBLE
            } else {
                itemView.progressBarPopular?.visibility = View.GONE
            }

            if (networkState != null && networkState == Network.ERROR) {
                itemView.tvConnectionErrorPopular?.visibility = View.VISIBLE
                itemView.tvConnectionErrorPopular?.text = networkState.msg
            } else if (networkState != null && networkState == Network.END) {
                itemView.tvConnectionErrorPopular?.visibility = View.VISIBLE
                itemView.tvConnectionErrorPopular?.text = networkState.msg
            } else {
                itemView.tvConnectionErrorPopular?.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(networkState: Network) {
        val previousState: Network? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()

        this.networkState = networkState
        val hasExtraRow: Boolean = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if(hadExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}