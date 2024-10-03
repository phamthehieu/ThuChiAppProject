package com.example.thuchiapp.views.home

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thuchiapp.controller.UserController
import com.example.thuchiapp.databinding.FragmentHomeBinding
import com.example.thuchiapp.model.GreetingMessageProvider

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", MODE_PRIVATE)
        userController = UserController(sharedPreferences)

        setupLayout()

        return binding.root
    }

    private fun setupLayout() {
        binding.namePerson.text = userController.getFullName()
        binding.textHeader.text = GreetingMessageProvider().getGreetingMessage(requireContext())
        binding.btnSearch.setOnClickListener {

        }
    }
}
