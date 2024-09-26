package com.example.thuchiapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thuchiapp.R
import com.example.thuchiapp.entity.PendingJars
import java.text.DecimalFormat

class ConsumerCategoryAdapter(
    private val pendingJarsList: List<PendingJars>,
    private val total: String,
    private val language: String
) : RecyclerView.Adapter<ConsumerCategoryAdapter.ConsumerCategoryViewHolder>() {

    inner class ConsumerCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val amountConsumerCategory: TextView = itemView.findViewById(R.id.amountConsumerCategory)
        private val nameConsumerCategory: TextView = itemView.findViewById(R.id.nameConsumerCategory)
        private val percent: TextView = itemView.findViewById(R.id.percent)
        private val moneyType: TextView = itemView.findViewById(R.id.moneyType)
        private val seekBar: ProgressBar = itemView.findViewById(R.id.seek_bar)

        @SuppressLint("SetTextI18n")
        fun bind(pendingJar: PendingJars) {
            icon.setImageResource(pendingJar.iconResource)
            nameConsumerCategory.text = pendingJar.title
            val expenseFormatter = DecimalFormat("#,###.##")
            amountConsumerCategory.text = expenseFormatter.format(pendingJar.amount.toDouble())
            percent.text = "${pendingJar.percent} %"
            seekBar.progress = pendingJar.percent

            if (language == "vi") {
                moneyType.text = itemView.context.getString(R.string.vnd)
            } else {
                moneyType.text = itemView.context.getString(R.string.usd)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumerCategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_consumer_category, parent, false)
        return ConsumerCategoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pendingJarsList.size
    }

    override fun onBindViewHolder(holder: ConsumerCategoryViewHolder, position: Int) {
        val pendingJar = pendingJarsList[position]
        holder.bind(pendingJar)
    }
}
