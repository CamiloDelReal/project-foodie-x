package org.xapps.apps.foodiex.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "guid")
    val guid: Long = 0,

    @ColumnInfo(name = "recipe_id")
    var recipeId: Long,

    @ColumnInfo(name = "bookmark")
    var bookmark: Boolean
)