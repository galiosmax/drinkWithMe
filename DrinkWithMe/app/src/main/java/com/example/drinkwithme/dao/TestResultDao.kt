package com.example.drinkwithme.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.model.TestResult

@Dao
interface TestResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(testResult: TestResult): Long

    @Update
    fun update(testResult: TestResult)

    @Delete
    fun delete(testResult: TestResult)

    @Query("DELETE from test_results_table")
    fun deleteAll()

    @Query("SELECT * from test_results_table WHERE test_results_table.time = (SELECT MAX(time) from drink_results_table)")
    fun getMostRecentResult(): LiveData<TestResult>

    @Query("SELECT * from test_results_table ORDER BY time DESC")
    fun getAll(): LiveData<List<TestResult>>

    @Query("SELECT * from test_results_table WHERE test_results_table.id=:id")
    fun getByID(id: Int): LiveData<TestResult>

    @Query("SELECT count(*) from test_results_table")
    fun count(): Int

}