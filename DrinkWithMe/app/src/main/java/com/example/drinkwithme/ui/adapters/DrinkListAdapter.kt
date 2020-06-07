package com.example.drinkwithme.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drinkwithme.R
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.ui.fragments.DrinkDetailsFragment
import com.example.drinkwithme.main.MainActivity
import java.util.*
import kotlin.collections.ArrayList

class DrinkListAdapter(private var context: Context) :
    RecyclerView.Adapter<DrinkListAdapter.DrinkViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    var isSaving = false

    var mDrinkList: List<Drink> = emptyList()

    var mDrinkListCopy = mDrinkList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val mItemView = mInflater.inflate(R.layout.drinklist_item, parent, false)
        return DrinkViewHolder(mItemView)
    }

    override fun getItemCount(): Int {
        return mDrinkList.size
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val mCurrent = mDrinkList[position]
        Glide.with(mInflater.context).load(mCurrent.drinkImageUrl + "/preview")
            .into(holder.imageView)
        holder.drinkNameText.text = mCurrent.drinkName
        val textAlcohol =
            mInflater.context.getString(R.string.alcohol_default, mCurrent.drinkAlcohol)
        holder.drinkAlcoholText.text = textAlcohol
    }

    fun filter(text: String) {
        val filteredList: ArrayList<Drink> = ArrayList()
        if (text.isBlank()) {
            filteredList.addAll(mDrinkListCopy)
        } else {
            val loweredText = text.toLowerCase(Locale.ROOT)
            mDrinkListCopy.forEach { drink ->
                if (drink.drinkName!!.toLowerCase(Locale.ROOT).contains(loweredText)) {
                    filteredList.add(drink)
                }
            }
        }
        mDrinkList = filteredList
        notifyDataSetChanged()
    }

    inner class DrinkViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val imageView: ImageView = itemView.findViewById(R.id.drinkPreviewImage_view)
        val drinkNameText: TextView = itemView.findViewById(R.id.drinkNamePreview_text)
        val drinkAlcoholText: TextView = itemView.findViewById(R.id.drinkAlcoholPreview_text)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val mPosition = layoutPosition
            val element = mDrinkList[mPosition]

            val drinkDetailsFragment = DrinkDetailsFragment.newInstance()
            val bundle = Bundle()
            bundle.putSerializable("drink", element)
            bundle.putBoolean("isSaving", isSaving)
            drinkDetailsFragment.arguments = bundle
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, drinkDetailsFragment).addToBackStack(null).commit()
        }
    }

}