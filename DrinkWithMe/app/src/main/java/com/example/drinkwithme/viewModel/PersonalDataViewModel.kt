package com.example.drinkwithme.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drinkwithme.R
import kotlin.math.max
import kotlin.math.min

const val WEIGHT_KEY = "weight"
const val HEIGHT_KEY = "height"
const val GENDER_KEY = "gender"

class PersonalDataViewModel(application: Application) : AndroidViewModel(application),
    AdapterView.OnItemSelectedListener {

    private var mPreferences: SharedPreferences
    private val sharedPreferencesFile = "com.example.drinkwithme"

    var mWeight: ObservableField<String> = ObservableField()
    var mHeight: ObservableField<String> = ObservableField()
    var mGenderPosition = MutableLiveData(0)

    init {
        mPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFile, AppCompatActivity.MODE_PRIVATE)
        val weight = mPreferences.getInt(WEIGHT_KEY, -1)
        if (weight != -1) {
            mWeight.set(weight.toString())
        }

        val height = mPreferences.getInt(HEIGHT_KEY, -1)
        if (height != -1) {
            mHeight.set(height.toString())
        }

        val gender = mPreferences.getInt(GENDER_KEY, -1)
        if (gender != -1) {
            mGenderPosition.value = gender
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing here
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mGenderPosition.value = position
    }

    fun onDone() {
        val preferencesEditor = mPreferences.edit()

        val weightInserted = mWeight.get()!!.toInt()
        if (weightInserted < 30 || weightInserted > 200) {
            Toast.makeText(getApplication(), "Weight $weightInserted is not in bounds: from 30 to 200", Toast.LENGTH_LONG).show()
        }

        val weight = max(30, min(weightInserted, 200))

        preferencesEditor.putInt(WEIGHT_KEY, weight)

        val heightInserted = mHeight.get()!!.toInt()
        if (heightInserted < 100 || heightInserted > 250) {
            Toast.makeText(getApplication(), "Weight $heightInserted is not in bounds: from 100 to 250", Toast.LENGTH_LONG).show()
        }

        val height = max(100, min(mHeight.get()!!.toInt(), 250))
        preferencesEditor.putInt(HEIGHT_KEY, height)

        preferencesEditor.putInt(GENDER_KEY, mGenderPosition.value!!)

        preferencesEditor.apply()
    }

}
