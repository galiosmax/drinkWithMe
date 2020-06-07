package com.example.drinkwithme.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.drinkwithme.dao.DrinkDao
import com.example.drinkwithme.dao.DrinkResultDao
import com.example.drinkwithme.dao.TestResultDao
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.model.TestResult

const val databaseName = "drinkWithMe_database"

@Database(entities = [Drink::class, DrinkResult::class, TestResult::class], version = 1)
public abstract class DrinkWithMeRoomDatabase: RoomDatabase() {

    abstract fun drinkDao(): DrinkDao
    abstract fun drinkResultDao(): DrinkResultDao
    abstract fun testResultDao(): TestResultDao

    companion object {

        @Volatile
        private var INSTANCE: DrinkWithMeRoomDatabase? = null

        fun getDatabase(context: Context): DrinkWithMeRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrinkWithMeRoomDatabase::class.java,
                    databaseName
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}