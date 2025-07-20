package com.example.newsapplicationjetpackcompose.di

import android.content.Context
import com.example.newsapplicationjetpackcompose.data.remote.api.NewsApi
import com.example.newsapplicationjetpackcompose.data.remote.dao.FirebaseDao
import com.example.newsapplicationjetpackcompose.data.repository.NewsRepositoryImplementation
import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsRepositoryModule {

    @Provides
    @Singleton
    fun providesNewsRepository(
        api: NewsApi,
        db: FirebaseDao,
        @ApplicationContext context: Context
    ): NewsRepository{
        return NewsRepositoryImplementation(api, db, context)
    }

}