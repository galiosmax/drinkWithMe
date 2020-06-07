package com.example.drinkwithme.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class DrinkResultsViewModel(
    application: Application,
    private var mDrink: Drink,
    private var volume: Int
) : AndroidViewModel(application) {

    private lateinit var mPreferences: SharedPreferences
    private val sharedPreferencesFile = "com.example.drinkwithme"

    var mWeight: Int = 1
    var mGenderPosition: Int = 0

    private lateinit var drinkWithMeRepository: DrinkWithMeRepository

    var mVolume: ObservableField<Int> = ObservableField(volume)
    var mDrinkName: ObservableField<String> = ObservableField(mDrink.drinkName!!)
    var mAlcohol: ObservableField<Int> = ObservableField(mDrink.drinkAlcohol)

    var mPreviousDrinkResult: DrinkResult? = null
        set(value) {
            field = value
            mPreviousAlcoholContent.set(value?.ppm ?: 0.0)
            mNextAlcoholContent.set(countAlcoholContent(value))
        }

    var mPreviousAlcoholContent: ObservableField<Double> = ObservableField(0.0)
    var mNextAlcoholContent: ObservableField<Double> = ObservableField(0.0)

    init {
        loadDatabase()
        loadPreferences()
    }

    private fun loadDatabase() {
        val database = DrinkWithMeRoomDatabase.getDatabase(getApplication())
        drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(database.drinkDao(), database.drinkResultDao(), database.testResultDao())
    }

    private fun loadPreferences() {
        mPreferences = getApplication<Application>().getSharedPreferences(
            sharedPreferencesFile,
            AppCompatActivity.MODE_PRIVATE
        )
        val weight = mPreferences.getInt(WEIGHT_KEY, -1)
        if (weight != -1) {
            mWeight = weight
        }

        val gender = mPreferences.getInt(GENDER_KEY, -1)
        if (gender != -1) {
            mGenderPosition = gender
        }
    }

    private fun countAlcoholContent(value: DrinkResult?): Double {
        val reduceFactor = if (mGenderPosition == 0) 0.7 else 0.6

        val currentTime = System.currentTimeMillis()
        val hoursPassed = (currentTime - (value?.time ?: currentTime)) / 3600000.0

        val result = max(
            0.0,
            (value?.ppm ?: 0.0) - 0.15 * hoursPassed
        ) + ((mAlcohol.get()!! * 0.01 * mVolume.get()!!) / (mWeight * reduceFactor))
        return max(0.0, result)
    }

    fun getMostRecentDrinkResult(): LiveData<DrinkResult> {
        return drinkWithMeRepository.getMostRecentResult()
    }

    fun saveResult() = viewModelScope.launch(Dispatchers.IO) {
        val result =
            DrinkResult(mDrink, mNextAlcoholContent.get() ?: mPreviousAlcoholContent.get() ?: 0.0)
        drinkWithMeRepository.insertDrinkResult(result)
    }
}