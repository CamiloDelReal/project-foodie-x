package org.xapps.apps.foodiex.core.models

import androidx.databinding.ObservableField
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "guid")
    var guid: Long = 0,

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "image_type")
    val imageType: String,

    @ColumnInfo(name = "title")
    val title: String
) {

    @Ignore
    @Transient
    var bookmarked = ObservableField(false)

}