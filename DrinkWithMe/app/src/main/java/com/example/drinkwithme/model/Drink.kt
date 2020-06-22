package com.example.drinkwithme.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import java.io.Serializable;

@Entity(tableName = "drink_table")
class Drink: Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("idDrink")
    var id: Int? = null

    @ColumnInfo(name = "name")
    @SerializedName("strDrink")
    var drinkName: String? = null

    @ColumnInfo(name = "imageUrl")
    @SerializedName("strDrinkThumb")
    var drinkImageUrl: String? = null

    @ColumnInfo(name = "alcohol")
    var drinkAlcohol: Int = Random().nextInt(100)
}