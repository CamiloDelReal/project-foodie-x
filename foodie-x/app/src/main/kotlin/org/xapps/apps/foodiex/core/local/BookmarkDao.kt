package org.xapps.apps.foodiex.core.local

import androidx.room.*
import org.xapps.apps.foodiex.core.models.Bookmark


@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(bookmark: Bookmark): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: Bookmark): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsync(bookmarks: List<Bookmark>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmarks: List<Bookmark>): List<Long>

    @Transaction
    @Query("SELECT * FROM bookmarks ORDER BY timestamp ASC")
    fun bookmarks(): List<Bookmark>

    @Transaction
    @Query("SELECT * FROM bookmarks WHERE recipe_id = :id")
    suspend fun bookmarkByRecipeIdAsync(id: Long): Bookmark?

    @Update
    suspend fun updateAsync(bookmark: Bookmark): Int

    @Update
    fun update(bookmark: Bookmark): Int

    @Update
    suspend fun updateAsync(bookmarks: List<Bookmark>): Int

    @Update
    fun update(bookmarks: List<Bookmark>): Int

    @Delete
    suspend fun deleteAsync(bookmark: Bookmark): Int

    @Delete
    fun delete(bookmark: Bookmark): Int

    @Delete
    suspend fun deleteAsync(bookmarks: List<Bookmark>): Int

    @Delete
    fun delete(bookmarks: List<Bookmark>): Int

}