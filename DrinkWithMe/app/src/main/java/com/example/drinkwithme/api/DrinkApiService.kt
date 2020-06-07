package com.example.drinkwithme.api

import com.example.drinkwithme.model.DrinkList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface DrinkApiService {

    @GET("filter.php")
    fun getAlcoholDrinks(@Query("a") alcohol: String?): Observable<DrinkList>

    @GET("search.php")
    fun getDrinksByFirstLetter(@Query("f") alcohol: Char?): Observable<DrinkList>

}