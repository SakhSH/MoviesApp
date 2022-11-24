package com.example.moviesapp.di

import com.example.moviesapp.data.api.ApiFactory
import com.example.moviesapp.data.api.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun providesMoviesApiService(): MoviesApiService = ApiFactory.apiService
}