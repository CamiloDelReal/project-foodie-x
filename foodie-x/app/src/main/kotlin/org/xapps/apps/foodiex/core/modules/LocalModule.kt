package org.xapps.apps.foodiex.core.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.xapps.apps.foodiex.core.local.SpoonacularDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    companion object {
        private const val FILENAME = "application_database.db"
    }

    @Singleton
    @Provides
    fun provideSpoonacularDatabase(@ApplicationContext context: Context): SpoonacularDatabase =
        Room.databaseBuilder(context, SpoonacularDatabase::class.java, FILENAME).build()

}