package com.example.thuchiapp.views.revenue_and_expenditure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thuchiapp.R
import com.example.thuchiapp.databinding.FragmentSpendingBinding

class SpendingFragment : Fragment() {

    private lateinit var binding: FragmentSpendingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpendingBinding.inflate(inflater, container, false)
        return binding.root
    }
}