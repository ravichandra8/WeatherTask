package com.ravi.weathertask.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ravi.weathertask.repository.local.WeatherDao
import com.ravi.weathertask.repository.local.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

        }
    }
//
private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE location "
                    + " ADD COLUMN isLocationAdded INTEGER NOT NULL DEFAULT 0")
        }
    }



    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): WeatherDatabase {
        return Room
            .databaseBuilder(
                context,
                WeatherDatabase::class.java,
                WeatherDatabase.DATABASE_NAME)
              .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
//                .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBlogDAO(blogDatabase: WeatherDatabase): WeatherDao {
        return blogDatabase.weatherDao()
    }
}