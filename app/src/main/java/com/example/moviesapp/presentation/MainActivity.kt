package com.example.moviesapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.R
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Default).launch {
            delay(5000)
            withContext(Dispatchers.Main) {
                MoviesActivity.startActivity(this@MainActivity)
            }
        }
    }
}