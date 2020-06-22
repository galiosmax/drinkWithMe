package com.example.drinkwithme.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.drinkwithme.model.TestResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase

class GameHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val drinkWithMeRepository: DrinkWithMeRepository

    init {
        val database = DrinkWithMeRoomDatabase.getDatabase(application)
        drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(database.drinkDao(), database.drinkResultDao(), database.testResultDao())
    }

    fun getAllTestResults() : LiveData<List<TestResult>> {
        return drinkWithMeRepository.getTestResultList()
    }
}