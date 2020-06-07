package com.example.drinkwithme.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drinkwithme.model.DrinkResult

@Dao
interface DrinkResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drinkResult: DrinkResult): Long

    @Update
    fun update(drinkResult: DrinkResult)

    @Delete
    fun delete(drinkResult: DrinkResult)

    @Query("DELETE from drink_results_table")
    fun deleteAll()

    @Query("SELECT * from drink_results_table WHERE drink_results_table.time = (SELECT MAX(time) from drink_results_table)")
    fun getMostRecentResult(): LiveData<DrinkResult>

    @Query("SELECT * from drink_results_table ORDER BY time DESC")
    fun getAll(): LiveData<List<DrinkResult>>

    @Query("SELECT * from drink_results_table WHERE drink_results_table.id=:id")
    fun getByID(id: Int): LiveData<DrinkResult>

    @Query("SELECT count(*) from drink_results_table")
    fun count(): Int

}