package com.example.drinkwithme.model

import com.google.gson.annotations.SerializedName

class DrinkList {

    @SerializedName("drinks")
    var drinkList: List<Drink>? = null

}