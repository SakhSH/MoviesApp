package com.example.moviesapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
    }

    override fun onBackPressed() {
        return
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(
                Intent(context, MoviesActivity::class.java)
            )
        }
    }
}