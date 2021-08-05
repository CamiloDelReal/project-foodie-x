package org.xapps.apps.foodiex.core.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.xapps.apps.foodiex.core.local.BookmarkDao
import org.xapps.apps.foodiex.core.local.RecipeDao
import org.xapps.apps.foodiex.core.remote.SpoonacularApi
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import org.xapps.apps.foodiex.core.utils.ConnectivityTracker
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RecipesModule {

    companion object {
        private const val BASE_URL = "https://api.spoonacular.com/"
        private const val API_STARTING_OFFSET = 0
        private const val API_PAGE_SIZE = 50
        const val BASE_URL_IMAGES = "https://spoonacular.com/recipeImages/"
        const val DIMENSION_636x393 = "636x393"
    }

    @Singleton
    @Provides
    fun provideRecipesRepository(
        @ApplicationContext context: Context,
        connectivityTracker: ConnectivityTracker,
        spoonacularApi: SpoonacularApi,
        recipeDao: RecipeDao,
        bookmarkDao: BookmarkDao
    ): RecipesRepository =
        RecipesRepository(
            context = context,
            dispatcher = Dispatchers.IO,
            connectivityTracker = connectivityTracker,
            spoonacularApi = spoonacularApi,
            recipeDao = recipeDao,
            bookmarkDao = bookmarkDao,
            pageStartingOffset = API_STARTING_OFFSET,
            pageSize = API_PAGE_SIZE
        )

}