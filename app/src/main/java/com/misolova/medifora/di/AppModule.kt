package com.misolova.medifora.di

import android.content.Context
import androidx.room.Room
import com.misolova.medifora.data.source.local.MediforaDao
import com.misolova.medifora.data.source.local.MediforaDatabase
import com.misolova.medifora.util.Constants.MEDIFORA_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    // Provide DB and Dao for application lifetime and only a singleton instance of both
    @Singleton
    @Provides
    fun provideMediforaDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        MediforaDatabase::class.java,
        MEDIFORA_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideMediforaDao(db: MediforaDatabase) = db.getMediforaRoomDao()

}