package com.mergenc.imovs.view.ui.castdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mergenc.imovs.R
import com.mergenc.imovs.databinding.ActivityCastDetailsBinding
import com.mergenc.imovs.databinding.ActivityMovieDetailsBinding
import com.mergenc.imovs.utils.api.POSTER_BASE_URL
import com.mergenc.imovs.view.ui.homepage.HomepageActivity

class CastDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCastDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCastDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name: String? = intent.getStringExtra("name")
        val knownForDepartment: String? = intent.getStringExtra("knownForDepartment")
        val originalName: String? = intent.getStringExtra("originalName")
        val characterName: String? = intent.getStringExtra("characterName")
        val homepage: String? = intent.getStringExtra("homepage")
        val profilePath: String? = intent.getStringExtra("profilePath")

        binding.tvTitleName.text = name
        binding.tvDepartment.text = knownForDepartment
        binding.tvOriginalName.text = originalName
        binding.tvCharacterName.text = characterName

        val castProfile: String = POSTER_BASE_URL + profilePath

        Glide.with(this).load(castProfile).into(binding.ivProfilePath)

        binding.cvHomepage.setOnClickListener {
            if (homepage != null) {
                Log.d("homepage", homepage)
                if (!homepage.contains("http")) {
                    Log.d("asdasdasgga","hehe")
                    Toast.makeText(this, "There is no homepage... Sad.", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, HomepageActivity::class.java)
                    intent.putExtra("homepageUrl", homepage)
                    this.startActivity(intent)
                }
            }
        }

    }
}