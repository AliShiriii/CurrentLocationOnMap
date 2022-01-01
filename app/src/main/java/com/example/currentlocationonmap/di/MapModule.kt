package com.example.currentlocationonmap.di

import android.content.Context
import androidx.room.Room
import com.example.currentlocationonmap.db.MapDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Provides
    @Singleton
    fun provideFavoriteMoveDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MapDataBase::class.java,
            "map_db"
        ).build()

    @Provides
    @Singleton
    fun provideFavoriteMoveDao(db: MapDataBase) = db.getDao()

}