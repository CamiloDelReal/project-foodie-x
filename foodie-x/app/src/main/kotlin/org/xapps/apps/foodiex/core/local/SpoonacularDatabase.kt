package org.xapps.apps.foodiex.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.xapps.apps.foodiex.core.models.Bookmark
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.PopularMeal
import org.xapps.apps.foodiex.core.models.Recipe


@Database(
    entities = [
        Recipe::class,
        Bookmark::class,
        PopularDrink::class,
        PopularMeal::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SpoonacularDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun popularDrinkDao(): PopularDrinkDao

    abstract fun popularMealDao(): PopularMealDao

}