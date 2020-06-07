package com.example.drinkwithme.repository

import androidx.lifecycle.LiveData
import com.example.drinkwithme.api.DrinkApiClient
import com.example.drinkwithme.api.DrinkApiService
import com.example.drinkwithme.dao.DrinkDao
import com.example.drinkwithme.dao.DrinkResultDao
import com.example.drinkwithme.dao.TestResultDao
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.model.TestResult
import com.example.drinkwithme.ui.fragments.ALCOHOLIC
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DrinkWithMeRepository private constructor(
    private val drinkDao: DrinkDao,
    private val drinkResultDao: DrinkResultDao,
    private val testResultDao: TestResultDao
) {

    private val retrofit = DrinkApiClient.newInstance().getRetrofit()

    fun getDrinkList(): LiveData<List<Drink>> {
        GlobalScope.launch(Dispatchers.IO) {
            if (drinkDao.count() == 0) {
                loadDrinkList()
            }
        }
        return drinkDao.getAll()
    }

    fun getDrinkList(text: String): LiveData<List<Drink>> {
        GlobalScope.launch(Dispatchers.IO) {
            if (!text.isBlank()) loadDrinkList(text)
        }
        return drinkDao.getAll()
    }

    private fun loadDrinkList() {
        val apiService: DrinkApiService = retrofit.create(DrinkApiService::class.java)

        val drinksList =
            apiService.getAlcoholDrinks(ALCOHOLIC).subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe { data ->
                GlobalScope.launch(Dispatchers.IO) {
                    data.drinkList?.forEach { drink ->
                        insertDrink(drink)
                    }
                }
            }
    }

    private fun loadDrinkList(text: String) {
        val apiService: DrinkApiService = retrofit.create(DrinkApiService::class.java)

        val firstLetter = text[0]

        val drinksList =
            apiService.getDrinksByFirstLetter(firstLetter).subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe { data ->
                GlobalScope.launch(Dispatchers.IO) {
                    data.drinkList?.forEach { drink ->
                        insertDrink(drink)
                    }
                }
            }
    }

    fun getDrinkResultList(): LiveData<List<DrinkResult>> {
        return drinkResultDao.getAll()
    }

    fun getTestResultList(): LiveData<List<TestResult>> {
        return testResultDao.getAll()
    }

    suspend fun insertDrinkResult(drinkResult: DrinkResult) {

        this.drinkResultDao.insert(drinkResult)
    }

    suspend fun insertDrink(drink: Drink) {
        drinkDao.insert(drink)
    }

    suspend fun insertTestResult(testResult: TestResult) {
        testResultDao.insert(testResult)
    }

    suspend fun updateDrink(drink: Drink) {
        drinkDao.update(drink)
    }

    fun getMostRecentResult(): LiveData<DrinkResult> {
        return drinkResultDao.getMostRecentResult()
    }

    fun getMostRecentTestResult(): LiveData<TestResult> {
        return testResultDao.getMostRecentResult()
    }

    companion object {
        @Volatile
        private var INSTANCE: DrinkWithMeRepository? = null

        fun getRepository(
            drinkDao: DrinkDao,
            drinkResultDao: DrinkResultDao,
            testResultDao: TestResultDao
        ): DrinkWithMeRepository {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = DrinkWithMeRepository(drinkDao, drinkResultDao, testResultDao)
                INSTANCE = instance
                return instance
            }
        }
    }

}