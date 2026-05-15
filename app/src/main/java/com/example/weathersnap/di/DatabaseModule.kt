package com.example.weathersnap.di


import android.content.Context
import androidx.room.Room
import com.example.weathersnap.data.local.ReportDao
import com.example.weathersnap.data.local.WeatherSnapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherSnapDatabase(
        @ApplicationContext context: Context
    ): WeatherSnapDatabase {
        return Room.databaseBuilder(
            context,
            WeatherSnapDatabase::class.java,
            "weather_snap_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDao(
        database: WeatherSnapDatabase
    ): ReportDao {
        return database.reportDao()
    }
}