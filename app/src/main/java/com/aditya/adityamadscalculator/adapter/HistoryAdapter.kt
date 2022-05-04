package com.aditya.adityamadscalculator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adityamadscalculator.R
import com.aditya.adityamadscalculator.models.HistoryData
import java.lang.String
import kotlin.Int

class HistoryAdapter
    (private val historyData: ArrayList<HistoryData>, private val context: Context) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCount.text = String.valueOf(historyData[position].count) + "."
        holder.tvExpression.text = historyData[position].expression.toString() + "  ="
        holder.tvResult.text = String.valueOf(historyData[position].result)
    }

    override fun getItemCount(): Int {
        return historyData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCount: TextView = itemView.findViewById<View>(R.id.textviewCount) as TextView
        var tvExpression: TextView = itemView.findViewById<View>(R.id.textviewExp) as TextView
        var tvResult: TextView = itemView.findViewById<View>(R.id.textviewRes) as TextView
    }
}