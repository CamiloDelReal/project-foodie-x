package org.xapps.apps.foodiex.core.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.PopularMeal


@Dao
interface PopularMealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsync(recipe: PopularMeal): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipe: PopularMeal): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsync(recipes: List<PopularMeal>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipes: List<PopularMeal>): List<Long>

    @Transaction
    @Query("SELECT * FROM popular_meals")
    fun recipesAsync(): Flow<List<PopularMeal>>

    @Transaction
    @Query("SELECT * FROM popular_meals")
    fun recipes(): List<PopularMeal>

    @Transaction
    @Query("SELECT * FROM popular_meals WHERE id = :id")
    fun recipeAsync(id: Long): Flow<PopularMeal>

    @Transaction
    @Query("SELECT * FROM popular_meals WHERE id = :id")
    fun recipe(id: Long): PopularMeal

    @Update
    suspend fun updateAsync(recipe: PopularMeal): Int

    @Update
    fun update(recipe: PopularMeal): Int

    @Update
    suspend fun updateAsync(recipes: List<PopularMeal>): Int

    @Update
    fun update(recipes: List<PopularMeal>): Int

    @Delete
    suspend fun deleteAsync(recipe: PopularMeal): Int

    @Delete
    fun delete(recipe: PopularMeal): Int

    @Delete
    suspend fun deleteAsync(recipes: List<PopularMeal>): Int

    @Delete
    fun delete(recipes: List<PopularMeal>): Int

    @Query("DELETE FROM popular_meals")
    suspend fun deleteAllAsync(): Int

    @Query("DELETE FROM popular_meals")
    fun deleteAll(): Int

}