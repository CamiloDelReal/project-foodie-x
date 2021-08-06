package org.xapps.apps.foodiex.core.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.xapps.apps.foodiex.core.models.Recipe


@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(recipes: List<Recipe>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipes: List<Recipe>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWithIgnoreAsync(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWithIgnoreAsync(recipes: List<Recipe>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(recipes: List<Recipe>): List<Long>

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY timestamp ASC")
    fun recipesAsync(): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY timestamp ASC")
    fun recipes(): List<Recipe>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun recipeAsync(id: Long): Flow<Recipe>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun recipe(id: Long): Recipe

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    suspend fun recipesPaginatedAsync(offset: Int, limit: Int): List<Recipe>

    @Transaction
    @Query("SELECT * FROM recipes,bookmarks WHERE recipes.id = bookmarks.recipe_id AND bookmarks.bookmark = 1 GROUP BY recipes.id ORDER BY bookmarks.timestamp ASC")
    fun recipesBookmarkedPaginatedAsync(): PagingSource<Int, Recipe>

    @Transaction
    @Query("SELECT * FROM recipes,popular_drinks WHERE recipes.id = popular_drinks.recipe_id GROUP BY recipes.id ORDER BY popular_drinks.guid ASC")
    fun recipesPopularDrinksAsync(): List<Recipe>

    @Transaction
    @Query("SELECT * FROM recipes,popular_meals WHERE recipes.id = popular_meals.recipe_id GROUP BY recipes.id ORDER BY popular_meals.guid ASC")
    fun recipesPopularMealsAsync(): List<Recipe>

    @Update
    suspend fun updateAsync(recipe: Recipe): Int

    @Update
    fun update(recipe: Recipe): Int

    @Update
    suspend fun updateAsync(recipes: List<Recipe>): Int

    @Update
    fun update(recipes: List<Recipe>): Int

    @Delete
    suspend fun deleteAsync(recipe: Recipe): Int

    @Delete
    fun delete(recipe: Recipe): Int

    @Delete
    suspend fun deleteAsync(recipes: List<Recipe>): Int

    @Delete
    fun delete(recipes: List<Recipe>): Int

}