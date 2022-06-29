package com.mergenc.imovs.view.ui.cast

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mergenc.imovs.R
import com.mergenc.imovs.model.Cast.Cast
import com.mergenc.imovs.model.Cast.CastResponse
import com.mergenc.imovs.model.PopularMovie
import com.mergenc.imovs.utils.api.POSTER_BASE_URL
import com.mergenc.imovs.view.ui.castdetails.CastDetailsActivity
import com.mergenc.imovs.view.ui.moviedetails.MovieDetailsActivity
import com.mergenc.imovs.view.ui.movieslist.PopularMoviesAdapter

class CastRecyclerViewAdapter(val cast: List<Cast>, val activity: Activity) :
    RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder>() {

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName = itemView.findViewById<TextView>(R.id.tvPersonName)
        val characterName = itemView.findViewById<TextView>(R.id.tvCharacterName)
        val image = itemView.findViewById<ImageView>(R.id.ivCastImage)

        fun bind(castDetails: Cast?, context: Context) {
            var movieDetailsActivity = context as MovieDetailsActivity
            var homepage = movieDetailsActivity.homepage

            itemView.setOnClickListener {
                val intent = Intent(context, CastDetailsActivity::class.java)
                if (castDetails != null) {
                    intent.putExtra("name", castDetails.name)
                    intent.putExtra("knownForDepartment", castDetails.knownForDepartment)
                    intent.putExtra("originalName", castDetails.originalName)
                    intent.putExtra("characterName", castDetails.character)
                    intent.putExtra("profilePath", castDetails.profilePath)
                    intent.putExtra("homepage", homepage)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast: Cast = cast[position]

        holder.characterName.setText(cast.character)
        holder.personName.setText(cast.name)
        val movieBackdropUrl: String = POSTER_BASE_URL + cast.profilePath
        Glide.with(holder.itemView.context)
            .load(movieBackdropUrl)
            .centerCrop()
            .into(holder.image)

        holder.bind(cast, activity)
    }

}
