package com.example.newsapplicationjetpackcompose.di

import com.example.newsapplicationjetpackcompose.data.remote.dao.FirebaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseDao {
        return FirebaseDao()
    }

}