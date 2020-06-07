package com.example.drinkwithme.model

import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "drink_results_table")
class DrinkResult() {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int? = null

    @Embedded(prefix = "drink")
    var drink: Drink? = null

    @ColumnInfo(name = "time")
    var time: Long? = null

    @ColumnInfo(name = "ppm")
    var ppm: Double? = null

    constructor(drink: Drink?, ppm: Double) : this() {
        this.drink = drink
        this.time = System.currentTimeMillis()
        this.ppm = ppm
    }

    constructor(drink: Drink, time: Long, ppm: Double) : this() {
        this.drink = drink
        this.time = time
        this.ppm = ppm
    }

}