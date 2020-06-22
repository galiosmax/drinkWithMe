package com.example.drinkwithme.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.drinkwithme.model.Drink

@Dao
interface DrinkDao {

    @Insert(onConflict = REPLACE)
    fun insert(drink: Drink): Long

    @Update
    fun update(drink: Drink)

    @Delete
    fun delete(drink: Drink)

    @Query("DELETE from drink_table")
    fun deleteAll()

    @Query("SELECT * from drink_table ORDER BY id")
    fun getAll(): LiveData<List<Drink>>

    @Query("SELECT * from drink_table WHERE drink_table.id=:id")
    fun getByID(id: Int): Drink

    @Query("SELECT count(*) from drink_table")
    fun count(): Int
}