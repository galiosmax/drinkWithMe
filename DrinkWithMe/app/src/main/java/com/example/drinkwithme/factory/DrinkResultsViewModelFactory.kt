package com.example.drinkwithme.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.viewModel.DrinkResultsViewModel

@Suppress("UNCHECKED_CAST")
class DrinkResultsViewModelFactory(
    private var application: Application,
    private var drink: Drink,
    private var volume: Int
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DrinkResultsViewModel(application, drink, volume) as T
    }
}