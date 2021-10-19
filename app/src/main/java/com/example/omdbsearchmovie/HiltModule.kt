package com.example.omdbsearchmovie

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    fun roomProvider(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "FavoriteMovie")
            .build()
    }

    @Provides
    fun retrofitProvider(): RetrofitInterfaceClass {
        val baseUrl = "https://www.omdbapi.com/"

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()).build()

        return retrofit.create(RetrofitInterfaceClass::class.java)
    }
}