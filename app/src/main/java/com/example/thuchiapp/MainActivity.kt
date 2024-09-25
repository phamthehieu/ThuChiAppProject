package com.example.thuchiapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.thuchiapp.databinding.ActivityMainBinding
import com.example.thuchiapp.views.home.HomeFragment
import com.example.thuchiapp.views.LibraryFragment
import com.example.thuchiapp.views.OnboardingActivity
import com.example.thuchiapp.views.ShortsFragment
import com.example.thuchiapp.views.SubscriptionFragment
import com.example.thuchiapp.views.revenue_and_expenditure.RevenueAndExpenditureActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.chart -> replaceFragment(ShortsFragment())
                R.id.report -> replaceFragment(SubscriptionFragment())
                R.id.person -> replaceFragment(LibraryFragment())
            }
            true
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, RevenueAndExpenditureActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}