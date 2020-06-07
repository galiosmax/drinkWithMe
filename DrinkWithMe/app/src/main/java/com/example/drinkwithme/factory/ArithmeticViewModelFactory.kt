package com.example.drinkwithme.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drinkwithme.viewModel.ArithmeticViewModel

@Suppress("UNCHECKED_CAST")
class ArithmeticViewModelFactory(
    private var application: Application,
    private var onFinished: () -> Unit
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArithmeticViewModel(application, onFinished) as T
    }

}