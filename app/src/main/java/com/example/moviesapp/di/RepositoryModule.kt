package com.example.moviesapp.di

import com.example.moviesapp.data.api.MoviesRepositoryImpl
import com.example.moviesapp.domain.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideMovieRepository(impl: MoviesRepositoryImpl): MoviesRepository
}