package com.arduia.exchangerates.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arduia.exchangerates.data.local.ExchangeRateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExchangeRateDatabase {
        return Room.databaseBuilder(
            context,
            ExchangeRateDatabase::class.java,
            ExchangeRateDatabase.DATABASE_NAME
        )
            .build()
    }

}