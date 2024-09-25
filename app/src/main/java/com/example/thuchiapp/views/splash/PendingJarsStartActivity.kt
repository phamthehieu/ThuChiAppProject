package com.example.thuchiapp.views.splash

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.airbnb.lottie.LottieDrawable
import com.example.thuchiapp.R
import com.example.thuchiapp.adapter.MoneySpinnerAdapter
import com.example.thuchiapp.data.NameMoneyItem
import com.example.thuchiapp.databinding.ActivityPendingJarsStartBinding
import com.example.thuchiapp.viewModel.PendingJarsViewModel
import com.example.thuchiapp.viewModel.PendingJarsViewModelFactory
import com.example.thuchiapp.views.component.KeyBoardBottomSheetFragment
import com.example.thuchiapp.views.customs.AnimationUtils

class PendingJarsStartActivity : AppCompatActivity(), KeyBoardBottomSheetFragment.OnCalculationCompleteListener {

    private lateinit var binding: ActivityPendingJarsStartBinding
    private lateinit var adapter: MoneySpinnerAdapter
    private var total: String = ""

    private val pendingJarsViewModel: PendingJarsViewModel by viewModels {
        PendingJarsViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingJarsStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLottieAnimation()
        setupSpinner()
        setupLayout()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        pendingJarsViewModel.allPendingJars.observe(this) { pendingJars ->
            Log.d("Hieu35", "PendingJars: $pendingJars")
        }
    }

    private fun setupLayout() {
        binding.estimatedAmount.setOnClickListener {
            val keyboardFragment = KeyBoardBottomSheetFragment()

            val totalWithoutDots = total.replace(".", "")

            val bundle = Bundle()
            bundle.putString("total", totalWithoutDots)

            keyboardFragment.arguments = bundle

            keyboardFragment.show(supportFragmentManager, "KeyBoardBottomSheetFragment")
        }

        binding.btnSuccess.setOnClickListener {

        }
    }

    private fun setupLottieAnimation() {
        binding.lottieAnimationView.apply {
            setAnimation("animation_pending.json")
            speed = 1.0f
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    private fun setupSpinner() {
        val items = listOf(
            NameMoneyItem(R.string.vnd),
            NameMoneyItem(R.string.usd)
        )

        adapter = MoneySpinnerAdapter(this, items)
        binding.spinnerOnboarding.adapter = adapter
    }

    override fun onCalculationComplete(result: String) {
        total = result
        binding.estimatedAmount.text = result

        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("estimatedAmount", total)
        editor.apply()
    }

    override fun onBottomSheetDismissed() {
        val totalWithoutDots = total.replace(".", "")
        val totalValue = totalWithoutDots.toIntOrNull()

        if (totalValue != null && totalValue > 0) {
            if (binding.layoutSlogan.visibility == View.VISIBLE) {
                AnimationUtils.collapseLayout(binding.layoutSlogan)
            }
        } else {
            if (binding.layoutSlogan.visibility == View.GONE) {
                AnimationUtils.expandLayout(binding.layoutSlogan)
            }
        }
    }

}
