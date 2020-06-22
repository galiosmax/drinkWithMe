package com.example.drinkwithme.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.viewModel.DrinkDetailsViewModel

@Suppress("UNCHECKED_CAST")
class DrinkDetailsViewModelFactory(private var drink: Drink) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DrinkDetailsViewModel(drink) as T
    }
}