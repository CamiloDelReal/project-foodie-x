package org.xapps.apps.foodiex.core.models

import androidx.databinding.ObservableField
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity(tableName = "popular_drinks")
data class PopularDrink(
    @Transient
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "guid")
    var guid: Long = 0,

    @Json(name = "id")
    @ColumnInfo(name = "id")
    val id: Long,

    @Json(name = "image")
    @ColumnInfo(name = "image")
    val image: String,

    @Json(name = "imageType")
    @ColumnInfo(name = "image_type")
    val imageType: String,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    val title: String
) {

    @Ignore
    @Transient
    var bookmarked = ObservableField(false)

}