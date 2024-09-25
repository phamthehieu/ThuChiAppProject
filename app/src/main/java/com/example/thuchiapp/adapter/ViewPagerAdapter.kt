package com.example.thuchiapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.thuchiapp.views.revenue_and_expenditure.IncomeFragment
import com.example.thuchiapp.views.revenue_and_expenditure.SpendingFragment
import java.time.LocalDate

class ViewPagerAdapter(activity: FragmentActivity ) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = SpendingFragment()
                fragment
            }

            1 -> {
                val fragment = IncomeFragment()
                fragment
            }


            else -> SpendingFragment()
        }
    }
}