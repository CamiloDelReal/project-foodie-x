package org.xapps.apps.foodiex.core.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.xapps.apps.foodiex.core.local.*
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

    @Singleton
    @Provides
    fun provideRecipeDao(database: SpoonacularDatabase): RecipeDao =
        database.recipeDao()

    @Singleton
    @Provides
    fun provideBookmarkDao(database: SpoonacularDatabase): BookmarkDao =
        database.bookmarkDao()

    @Singleton
    @Provides
    fun providePopularDrinkDao(database: SpoonacularDatabase): PopularDrinkDao =
        database.popularDrinkDao()

    @Singleton
    @Provides
    fun providePopularMealDao(database: SpoonacularDatabase): PopularMealDao =
        database.popularMealDao()

}