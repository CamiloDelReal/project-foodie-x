package org.xapps.apps.foodiex.core.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PopularMealResponse(
    @Json(name = "number")
    val number: Int,

    @Json(name = "offset")
    val offset: Int,

    @Json(name = "totalResults")
    val totalResults: Int,

    @Json(name = "results")
    val results: List<PopularMeal>
)