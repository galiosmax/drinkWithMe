package com.example.drinkwithme.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.drinkwithme.model.Drink

class DrinkDetailsViewModel(mDrink: Drink) : ViewModel() {

    var mVolume: ObservableField<String> = ObservableField()
    var mDrinkName : ObservableField<String> = ObservableField(mDrink.drinkName!!)
    var mAlcohol : ObservableField<Int> = ObservableField(mDrink.drinkAlcohol)
}
