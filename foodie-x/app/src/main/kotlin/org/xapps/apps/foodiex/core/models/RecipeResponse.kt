package org.xapps.apps.foodiex.core.models


data class RecipeResponse(
    val number: Int,
    val offset: Int,
    val totalResults: Int,
    val results: List<Recipe>
)