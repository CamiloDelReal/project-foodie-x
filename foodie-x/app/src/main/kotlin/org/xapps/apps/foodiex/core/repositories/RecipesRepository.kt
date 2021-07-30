package org.xapps.apps.foodiex.core.repositories

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import org.xapps.apps.foodiex.core.remote.SpoonacularApi
import javax.inject.Inject


class RecipesRepository @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val spoonacularApi: SpoonacularApi
) {



}