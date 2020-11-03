package com.misolova.medifora.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.misolova.medifora.data.source.local.MediforaDatabase
import com.misolova.medifora.data.source.remote.FirebaseSource
import com.misolova.medifora.util.Constants.DARK_STATUS
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import com.misolova.medifora.util.Constants.MEDIFORA_DB_NAME
import com.misolova.medifora.util.Constants.SHARED_PREFERENCES_NAME
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
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMediforaDao(db: MediforaDatabase) = db.getMediforaRoomDao()

    @Singleton
    @Provides
    fun provideFirebaseSource() = FirebaseSource()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)!!

    @Singleton
    @Provides
    fun provideUserStatus(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(KEY_USER_STATUS, false)

    @Singleton
    @Provides
    fun provideUserId(sharedPreferences: SharedPreferences) = sharedPreferences.getString(KEY_USER_ID, "") ?: ""

    @Singleton
    @Provides
    fun provideDarkMode(sharedPreferences: SharedPreferences) = sharedPreferences.getInt(DARK_STATUS, 0)

}