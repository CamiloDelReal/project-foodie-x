package org.xapps.apps.foodiex.core.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.xapps.apps.foodiex.core.models.PopularDrink


@Dao
interface PopularDrinkDao {

    @Insert
    suspend fun insertAsync(recipe: PopularDrink): Long

    @Insert
    fun insert(recipe: PopularDrink): Long

    @Insert
    suspend fun insertAsync(recipes: List<PopularDrink>): List<Long>

    @Insert
    fun insert(recipes: List<PopularDrink>): List<Long>

    @Transaction
    @Query("SELECT * FROM popular_drinks ORDER BY timestamp ASC")
    fun recipesAsync(): Flow<List<PopularDrink>>

    @Transaction
    @Query("SELECT * FROM popular_drinks ORDER BY timestamp ASC")
    fun recipes(): List<PopularDrink>

    @Transaction
    @Query("SELECT * FROM popular_drinks WHERE guid = :id")
    fun recipeAsync(id: Long): Flow<PopularDrink>

    @Transaction
    @Query("SELECT * FROM popular_drinks WHERE guid = :id")
    fun recipe(id: Long): PopularDrink

    @Update
    suspend fun updateAsync(recipe: PopularDrink): Int

    @Update
    fun update(recipe: PopularDrink): Int

    @Update
    suspend fun updateAsync(recipes: List<PopularDrink>): Int

    @Update
    fun update(recipes: List<PopularDrink>): Int

    @Delete
    suspend fun deleteAsync(recipe: PopularDrink): Int

    @Delete
    fun delete(recipe: PopularDrink): Int

    @Delete
    suspend fun deleteAsync(recipes: List<PopularDrink>): Int

    @Delete
    fun delete(recipes: List<PopularDrink>): Int

    @Query("DELETE FROM popular_drinks")
    suspend fun deleteAllAsync(): Int

    @Query("DELETE FROM popular_drinks")
    fun deleteAll(): Int

}