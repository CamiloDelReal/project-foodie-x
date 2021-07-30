package org.xapps.apps.foodiex.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.xapps.apps.foodiex.core.models.Dummy

@Database(
    entities = [
        Dummy::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SpoonacularDatabase: RoomDatabase() {


}