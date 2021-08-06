package org.xapps.apps.foodiex.core.repositories

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.xapps.apps.foodiex.BuildConfig
import org.xapps.apps.foodiex.core.exceptions.QuotaHasBeenReachException
import org.xapps.apps.foodiex.core.local.BookmarkDao
import org.xapps.apps.foodiex.core.local.PopularDrinkDao
import org.xapps.apps.foodiex.core.local.PopularMealDao
import org.xapps.apps.foodiex.core.local.RecipeDao
import org.xapps.apps.foodiex.core.models.Bookmark
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.PopularMeal
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.remote.SpoonacularApi
import org.xapps.apps.foodiex.core.utils.*
import retrofit2.HttpException
import javax.inject.Inject


class RecipesRepository @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val connectivityTracker: ConnectivityTracker,
    private val spoonacularApi: SpoonacularApi,
    private val recipeDao: RecipeDao,
    private val bookmarkDao: BookmarkDao,
    private val popularDrinkDao: PopularDrinkDao,
    private val popularMealDao: PopularMealDao,
    private val pageStartingOffset: Int,
    private val pageSize: Int,
    private val ratedPageSize: Int
) {

    fun recipes(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RecipePagingSource(
                    dispatcher = dispatcher,
                    connectivityTracker = connectivityTracker,
                    api = spoonacularApi,
                    dao = recipeDao,
                    pageStartingOffset = pageStartingOffset,
                    pageSize = pageSize
                )
            }
        ).flow.flowOn(dispatcher)
    }

    fun recipesBookmarkedPaginated(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                recipeDao.recipesBookmarkedPaginatedAsync()
            }
        ).flow.flowOn(dispatcher)
    }

    suspend fun checkBookmark(recipe: Recipe): Either<Failure, Bookmark> = withContext(dispatcher) {
        try {
            val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
            if(bookmark != null) {
                recipe.bookmarked.set(bookmark.bookmark)
                bookmark.toSuccess()
            } else {
                Bookmark(recipeId = recipe.id, bookmark = false).toSuccess()
            }
        } catch(ex: Exception) {
            error<RecipesRepository>(ex, "Error captured")
            Failure.Exception(ex).toError()
        }
    }

    suspend fun insertBookmark(recipe: Recipe): Either<Failure, Bookmark> = withContext(dispatcher) {
        try {
            var bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
            if(bookmark != null) {
                bookmark.bookmark = true
                val count = bookmarkDao.updateAsync(bookmark)
                if(count == 1) {
                    recipe.bookmarked.set(true)
                    bookmark.toSuccess()
                } else {
                    Failure.Database.toError()
                }
            } else {
                bookmark = Bookmark(recipeId = recipe.id, bookmark = true)
                val id = bookmarkDao.insertAsync(bookmark)
                if(id > 0) {
                    recipe.bookmarked.set(true)
                    bookmark.toSuccess()
                } else {
                    Failure.Database.toError()
                }
            }
        } catch (ex: Exception) {
            error<RecipesRepository>(ex, "Error captured")
            Failure.Exception(ex).toError()
        }
    }

    suspend fun removeBookmark(recipe: Recipe): Either<Failure, Bookmark> = withContext(dispatcher) {
        try {
            val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
            if(bookmark != null) {
                bookmark.bookmark = false
                val count = bookmarkDao.updateAsync(bookmark)
                if(count == 1) {
                    recipe.bookmarked.set(false)
                    bookmark.toSuccess()
                } else {
                    Failure.Database.toError()
                }
            } else {
                Bookmark(recipeId = recipe.id, bookmark = false).toSuccess()
            }
        } catch(ex: Exception) {
            error<RecipesRepository>(ex, "Exception captured")
            Failure.Exception(ex).toError()
        }
    }

    suspend fun popularDrinks(): Either<Failure, List<Recipe>> = withContext(dispatcher) {
        try {
            val recipes = if(connectivityTracker.isConnectedToInternet()) {

                val response = spoonacularApi.listPopularDrinks(
                    apiKey = BuildConfig.SPOONACULAR_API_KEY,
                    offset = pageStartingOffset,
                    number = ratedPageSize
                )
                if(response.results.isNotEmpty()) {
                    popularDrinkDao.deleteAllAsync()
                    val idsRecipes = recipeDao.insertWithIgnore(response.results)
                    val ids = popularDrinkDao.insertAsync(response.results.map { recipe -> PopularDrink(recipeId = recipe.id) })
                    if (response.results.size == ids.size) {
                        response.results.forEachIndexed { index, recipe ->
                            val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
                            if(bookmark != null) {
                                recipe.bookmarked.set(bookmark.bookmark)
                            }
                        }
                    } else {
                        error<RecipesRepository>("Error saving recipes in database. Skipping updating cache")
                    }
                }
                response.results
            } else {
                val recipes = recipeDao.recipesPopularDrinksAsync()
                recipes.forEachIndexed { index, recipe ->
                    val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
                    if(bookmark != null) {
                        recipe.bookmarked.set(bookmark.bookmark)
                    }
                }
                recipes
            }
            recipes.toSuccess()
        } catch (ex: HttpException) {
            error<RecipesRepository>(ex, "Exception captured")
            Failure.Exception(QuotaHasBeenReachException(ex.localizedMessage)).toError()
        } catch(ex: Exception) {
            error<RecipesRepository>(ex, "Exception captured")
            Failure.Exception(ex).toError()
        }
    }

    suspend fun popularMeals(): Either<Failure, List<Recipe>> = withContext(dispatcher) {
        try {
            val recipes = if(connectivityTracker.isConnectedToInternet()) {

                val response = spoonacularApi.listPopularMeals(
                    apiKey = BuildConfig.SPOONACULAR_API_KEY,
                    offset = pageStartingOffset,
                    number = ratedPageSize
                )
                if(response.results.isNotEmpty()) {
                    popularMealDao.deleteAllAsync()
                    val idsRecipes = recipeDao.insertWithIgnore(response.results)
                    val ids = popularMealDao.insertAsync(response.results.map { recipe -> PopularMeal(recipeId = recipe.id) })
                    if (response.results.size == ids.size) {
                        response.results.forEachIndexed { index, recipe ->
                            val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
                            if(bookmark != null) {
                                recipe.bookmarked.set(bookmark.bookmark)
                            }
                        }
                    } else {
                        error<RecipesRepository>("Error saving recipes in database. Skipping updating cache")
                    }
                }
                response.results
            } else {
                val recipes = recipeDao.recipesPopularMealsAsync()
                recipes.forEachIndexed { index, recipe ->
                    val bookmark = bookmarkDao.bookmarkByRecipeIdAsync(recipe.id)
                    if(bookmark != null) {
                        recipe.bookmarked.set(bookmark.bookmark)
                    }
                }
                recipes
            }
            recipes.toSuccess()
        } catch (ex: HttpException) {
            error<RecipesRepository>(ex, "Exception captured")
            Failure.Exception(QuotaHasBeenReachException(ex.localizedMessage)).toError()
        } catch(ex: Exception) {
            error<RecipesRepository>(ex, "Exception captured")
            Failure.Exception(ex).toError()
        }
    }

}