package com.example.drinkwithme.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkwithme.R
import com.example.drinkwithme.model.TestResult
import java.sql.Timestamp
import java.util.*

class GameHistoryListAdapter(private var context: Context) :
    RecyclerView.Adapter<GameHistoryListAdapter.GameHistoryViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    var mTestResultList: List<TestResult> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameHistoryViewHolder {
        val mItemView = mInflater.inflate(R.layout.gamehistorylist_item, parent, false)
        return GameHistoryViewHolder(mItemView)
    }

    override fun getItemCount(): Int {
        return mTestResultList.size
    }

    override fun onBindViewHolder(holder: GameHistoryViewHolder, position: Int) {
        val mCurrent = mTestResultList[position]

        holder.gameHistoryScoreText.text = context.getString(R.string.game_score, mCurrent.score)
        holder.gameHistoryTimeText.text = Date(Timestamp(mCurrent.time!!).time).toString()
        val textAlcohol =
            mInflater.context.getString(R.string.alcohol_default, mCurrent.drunkPercent)
        holder.gameHistoryPpmText.text = textAlcohol
    }

    inner class GameHistoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val gameHistoryScoreText: TextView = itemView.findViewById(R.id.gameHistoryScore_text)
        val gameHistoryTimeText: TextView = itemView.findViewById(R.id.gameHistoryTime_text)
        val gameHistoryPpmText: TextView = itemView.findViewById(R.id.gameHistoryPpm_text)

    }

}