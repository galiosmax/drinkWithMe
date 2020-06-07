package com.example.drinkwithme.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drinkwithme.R
import com.example.drinkwithme.model.DrinkResult
import java.sql.Timestamp
import java.util.*

class HistoryListAdapter(private var context: Context) :
    RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    var mDrinkResultList: List<DrinkResult> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryListAdapter.HistoryViewHolder {
        val mItemView = mInflater.inflate(R.layout.historylist_item, parent, false)
        return HistoryViewHolder(mItemView)
    }

    override fun getItemCount(): Int {
        return mDrinkResultList.size
    }

    override fun onBindViewHolder(holder: HistoryListAdapter.HistoryViewHolder, position: Int) {
        val mCurrent = mDrinkResultList[position]

        if (mCurrent.drink != null) {
            Glide.with(mInflater.context).load(mCurrent.drink!!.drinkImageUrl + "/preview")
                .into(holder.imageView)
            holder.drinkHistoryNameText.text = mCurrent.drink!!.drinkName
        } else {
            holder.drinkHistoryNameText.text = context.getString(R.string.nothing)
        }
        holder.drinkHistoryTimeText.text = Date(Timestamp(mCurrent.time!!).time).toString()
        val textAlcohol =
            mInflater.context.getString(R.string.ppm_default, mCurrent.ppm)
        holder.historyPpmText.text = textAlcohol
    }

    inner class HistoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.drinkHistoryImage_view)
        val drinkHistoryNameText: TextView = itemView.findViewById(R.id.drinkHistoryName_text)
        val drinkHistoryTimeText: TextView = itemView.findViewById(R.id.drinkHistoryTime_text)
        val historyPpmText: TextView = itemView.findViewById(R.id.historyPpm_text)

    }

}