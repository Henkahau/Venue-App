package com.henkahau.venueapp.di

import com.henkahau.venueapp.data.api.FoursquareApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetroFit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/v2/venues/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesFoursquareApi(retrofit: Retrofit): FoursquareApi {
        return retrofit.create(FoursquareApi::class.java)
    }
}