package org.xapps.apps.foodiex.core.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import org.xapps.apps.foodiex.BuildConfig
import org.xapps.apps.foodiex.core.exceptions.QuotaHasBeenReachException
import org.xapps.apps.foodiex.core.local.RecipeDao
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.remote.SpoonacularApi
import org.xapps.apps.foodiex.core.utils.ConnectivityTracker
import org.xapps.apps.foodiex.core.utils.error
import retrofit2.HttpException


class RecipePagingSource constructor(
    private val dispatcher: CoroutineDispatcher,
    private val connectivityTracker: ConnectivityTracker,
    private val api: SpoonacularApi,
    private val dao: RecipeDao,
    private val pageStartingOffset: Int,
    private val pageSize: Int
): PagingSource<Int, Recipe>() {

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            try {
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            } catch (ex: Exception) {
                ex.printStackTrace()
                pageStartingOffset
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> = withContext(dispatcher) {
        val pageIndex = params.key ?: pageStartingOffset
        try {
            val recipes = if(connectivityTracker.isConnectedToInternet()) {
                val response = api.listRecipes(
                    apiKey = BuildConfig.SPOONACULAR_API_KEY,
                    offset = pageIndex * pageSize,
                    number = pageSize
                )
                if(response.results.isNotEmpty()) {
                    val ids = dao.insertAsync(response.results)
                    if (response.results.size != ids.size) {
                        error<RecipePagingSource>("Error saving recipes in database. Skipping updating cache")
                    }
                }
                response.results
            } else {
                dao.recipesPaginatedAsync(
                    offset = pageIndex * pageSize,
                    limit = pageSize
                )
            }
            val nextKey = if (recipes.isEmpty()) {
                null
            } else {
                pageIndex + 1
            }
            LoadResult.Page(
                data = recipes,
                prevKey = if (pageIndex == pageStartingOffset) null else pageIndex,
                nextKey = nextKey
            )
        } catch (ex: IOException) {
            error<RecipePagingSource>(ex, "Exception captured")
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            error<RecipePagingSource>(ex, "Exception captured")
            if(ex.response()?.code() == 402) {
                LoadResult.Error(QuotaHasBeenReachException(ex.response()?.message() ?: "Quota has been reached"))
            } else {
                LoadResult.Error(ex)
            }
        } catch (ex: Exception) {
            error<RecipePagingSource>(ex, "Exception captured")
            LoadResult.Error(ex)
        }
    }

}