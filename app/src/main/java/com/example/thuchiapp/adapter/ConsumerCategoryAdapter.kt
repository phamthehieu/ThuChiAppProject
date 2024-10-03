package com.example.thuchiapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thuchiapp.R
import com.example.thuchiapp.entity.PendingJars
import com.example.thuchiapp.utilities.OnAmountChangeListener
import com.example.thuchiapp.viewModel.PendingJarsAmountViewModel
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.math.ceil

class ConsumerCategoryAdapter(
    private val pendingJarsList: List<PendingJars>,
    private val total: String,
    private val language: String,
    private val listener: OnAmountChangeListener,
    private val viewModel: PendingJarsAmountViewModel
) : RecyclerView.Adapter<ConsumerCategoryAdapter.ConsumerCategoryViewHolder>() {

    inner class ConsumerCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val amountConsumerCategory: TextView = itemView.findViewById(R.id.amountConsumerCategory)
        private val nameConsumerCategory: TextView = itemView.findViewById(R.id.nameConsumerCategory)
        private val moneyType: TextView = itemView.findViewById(R.id.moneyType)
        private val seekBar: SeekBar = itemView.findViewById(R.id.seek_bar)
        private val dailyLimitValue: TextView = itemView.findViewById(R.id.daily_limit_value)
        private val currentDate = LocalDate.now()
        private val date = currentDate.lengthOfMonth()

        @SuppressLint("SetTextI18n")
        fun bind(pendingJar: PendingJars, position: Int) {
            icon.setImageResource(pendingJar.iconResource)
            nameConsumerCategory.text = pendingJar.title
            val expenseFormatter = DecimalFormat("#,###")
            amountConsumerCategory.text = expenseFormatter.format(pendingJar.amount.toDouble())

            val coreValuesSeekBar = pendingJar.percent
            seekBar.progress = pendingJar.percent

            val totalValue = pendingJar.amount.toDoubleOrNull() ?: 0.0
            val averageDay = if (date > 0) totalValue / date else 0.0
            val roundedAverageDay = (ceil(averageDay / 100) * 100).toInt()

            dailyLimitValue.text = "${expenseFormatter.format(roundedAverageDay)} / ${itemView.context.getString(R.string.date)}"

            moneyType.text = if (language == "vi") {
                itemView.context.getString(R.string.vnd)
            } else {
                itemView.context.getString(R.string.usd)
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                private var previousProgress: Int = pendingJar.percent // Lưu giá trị progress trước đó
                private var previousAmount: Double = pendingJar.amount.toDoubleOrNull() ?: 0.0 // Lưu giá trị amount trước đó

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        // Tính sự thay đổi (delta) dựa trên progress mới và previousProgress
                        val progressDelta = progress - previousProgress
                        val changePart = (progressDelta * total.toDouble() / 100) // Tính sự thay đổi của giá trị amount
                        // Cập nhật pendingJar với progress và amount mới
                        val newAmount = previousAmount + changePart
                        pendingJar.amount = newAmount.toString()
                        pendingJar.percent = progress
                        amountConsumerCategory.text = DecimalFormat("#,###").format(newAmount)
                        // Cập nhật listener để thông báo cập nhật UI
                        listener.onAmountUpdated(position, progress, newAmount.toString(), changePart)
                        // Cập nhật previousProgress và previousAmount để sử dụng cho lần kéo tiếp theo
                        previousProgress = progress
                        previousAmount = newAmount
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Không cần xử lý ở đây
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val progress = seekBar?.progress ?: coreValuesSeekBar
                    // Nếu progress đạt 100, đặt lại progress về coreValuesSeekBar
                    if (progress == 100 || progress ==  0) {
                        if (previousAmount > 0) {
                            seekBar?.progress = coreValuesSeekBar
                            seekBar?.invalidate()
                        }
                    } else {
                        viewModel.updatePercentAtPosition(position, previousAmount)
                    }

                    previousProgress = seekBar?.progress ?: coreValuesSeekBar
                    previousAmount = pendingJar.amount.toDoubleOrNull() ?: 0.0
                }
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumerCategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consumer_category, parent, false)
        return ConsumerCategoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pendingJarsList.size
    }

    override fun onBindViewHolder(holder: ConsumerCategoryViewHolder, position: Int) {
        val pendingJar = pendingJarsList[position]
        holder.bind(pendingJar, position)
    }
}

