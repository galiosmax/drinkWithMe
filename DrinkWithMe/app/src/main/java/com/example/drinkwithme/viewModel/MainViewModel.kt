package com.example.drinkwithme.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var drinkWithMeRepository: DrinkWithMeRepository

    var mPreviousDrinkResult: DrinkResult? = null
        set(value) {
            field = value
            val percent = countAlcoholContent(value)
            var state = "Shall we drink?"

            if (percent > 0 && percent <= 0.3) {
                state = "You're fine"
            } else if (percent > 0.3 && percent < -0.5) {
                state = "Light intoxication"
            } else if (percent > 0.5 && percent <= 1.5) {
                state = "Have fun!"
            } else if (percent > 1.5 && percent <= 2.5) {
                state = "Okay, you're drunk"
            } else if (percent > 2.5 && percent <= 3) {
                state = "You're really drunk"
            } else if (percent > 3 && percent <= 5) {
                state = "STOP! Really!"
            } else if (percent > 5) {
                state = "Well, you're probably dead"
            }
            mState.set(state)
            mAlcoholContent.set(percent)
        }

    var mState: ObservableField<String> = ObservableField("Shall we drink?")
    var mAlcoholContent: ObservableField<Double> = ObservableField(0.0)

    init {
        loadDatabase()
    }

    private fun loadDatabase() {
        val database = DrinkWithMeRoomDatabase.getDatabase(getApplication())
        drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(database.drinkDao(), database.drinkResultDao(), database.testResultDao())
    }

    private fun countAlcoholContent(value: DrinkResult?): Double {
        val currentTime = System.currentTimeMillis()
        val hoursPassed = (currentTime - (value?.time ?: currentTime)) / 3600000.0

        return max(0.0, (value?.ppm ?: 0.0) - 0.15 * hoursPassed)
    }

    fun getMostRecentDrinkResult(): LiveData<DrinkResult> {
        return drinkWithMeRepository.getMostRecentResult()
    }

    fun reset() = viewModelScope.launch(Dispatchers.IO) {

        val result = DrinkResult()
        result.time = System.currentTimeMillis()
        result.drink = null
        result.ppm = 0.0

        drinkWithMeRepository.insertDrinkResult(result)
    }

}
