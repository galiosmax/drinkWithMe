package com.example.drinkwithme.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results_table")
class TestResult() {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "score")
    var score: Int? = null

    @ColumnInfo(name = "time")
    var time: Long? = null

    @ColumnInfo(name = "drunk_percent")
    var drunkPercent: Int? = null

    constructor(score: Int, drunkPercent: Int) : this() {
        this.score = score
        this.time = System.currentTimeMillis()
        this.drunkPercent = drunkPercent
    }
}