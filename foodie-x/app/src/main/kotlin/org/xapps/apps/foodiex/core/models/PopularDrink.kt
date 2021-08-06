package org.xapps.apps.foodiex.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.xapps.apps.foodiex.core.utils.parseToString
import java.time.LocalDateTime


@Entity(tableName = "popular_drinks")
data class PopularDrink(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "guid")
    val guid: Long = 0,

    @ColumnInfo(name = "recipe_id")
    var recipeId: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: String = LocalDateTime.now().parseToString()
)