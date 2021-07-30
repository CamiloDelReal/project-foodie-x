package org.xapps.apps.foodiex.core.models


data class NutritionFilter(
    var name: String,
    var min: Float,
    var max: Float,
    var unit: String,
    var value: Float = max
)