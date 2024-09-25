package com.example.thuchiapp.views.revenue_and_expenditure

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.thuchiapp.R
import com.example.thuchiapp.adapter.ViewPagerAdapter
import com.example.thuchiapp.databinding.ActivityRevenueAndExpenditureBinding

class RevenueAndExpenditureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRevenueAndExpenditureBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueAndExpenditureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = binding.viewPagerTv

        setupLayout()
    }

    private fun setupLayout() {
        binding.tabExpense.setOnClickListener {
            viewPager.currentItem = 0
        }

        binding.tabIncome.setOnClickListener {
            viewPager.currentItem = 1
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTabBackground(position + 1)
            }
        })

    }

    private fun updateTabBackground(selectedTabNumber: Int) {
        val selectedColor = resources.getColor(R.color.black, this.theme)
        val unselectedColor = resources.getColor(R.color.white, this.theme)

        binding.tabExpense.apply {
            setBackgroundResource(if (selectedTabNumber == 1) R.drawable.round_back_green_left else R.drawable.round_back_white_left)
            setTextColor(if (selectedTabNumber == 1) unselectedColor else selectedColor)
        }
        binding.tabIncome.apply {
            setBackgroundResource(if (selectedTabNumber == 2) R.drawable.round_back_green_right else R.drawable.round_back_white_right)
            setTextColor(if (selectedTabNumber == 2) unselectedColor else selectedColor)
        }
    }

}