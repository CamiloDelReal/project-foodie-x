package org.xapps.apps.foodiex.core.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.xapps.apps.foodiex.core.remote.SpoonacularApi
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RecipesModule {

    @Singleton
    @Provides
    fun provideRecipesRepository(
        @ApplicationContext context: Context,
        spoonacularApi: SpoonacularApi
    ): RecipesRepository =
        RecipesRepository(
            context = context,
            dispatcher = Dispatchers.IO,
            spoonacularApi = spoonacularApi
        )

}