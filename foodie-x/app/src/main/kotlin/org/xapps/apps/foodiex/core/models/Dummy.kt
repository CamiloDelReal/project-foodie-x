package org.xapps.apps.foodiex.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Dummy {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}