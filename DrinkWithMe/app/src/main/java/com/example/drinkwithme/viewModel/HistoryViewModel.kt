package com.example.drinkwithme.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val drinkWithMeRepository: DrinkWithMeRepository

    init {
        val database = DrinkWithMeRoomDatabase.getDatabase(application)
        drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(database.drinkDao(), database.drinkResultDao(), database.testResultDao())
    }

    fun getAllDrinkResults() : LiveData<List<DrinkResult>> {
        return drinkWithMeRepository.getDrinkResultList()
    }

}