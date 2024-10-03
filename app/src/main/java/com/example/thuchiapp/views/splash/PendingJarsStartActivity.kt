package com.example.thuchiapp.views.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.thuchiapp.MainActivity
import com.example.thuchiapp.R
import com.example.thuchiapp.adapter.ConsumerCategoryAdapter
import com.example.thuchiapp.adapter.MoneySpinnerAdapter
import com.example.thuchiapp.data.NameMoneyItem
import com.example.thuchiapp.databinding.ActivityPendingJarsStartBinding
import com.example.thuchiapp.entity.PendingJars
import com.example.thuchiapp.utilities.OnAmountChangeListener
import com.example.thuchiapp.utilities.PreferencesHelper
import com.example.thuchiapp.viewModel.PendingJarsAmountViewModel
import com.example.thuchiapp.viewModel.PendingJarsViewModel
import com.example.thuchiapp.viewModel.PendingJarsViewModelFactory
import com.example.thuchiapp.views.component.KeyBoardBottomSheetFragment
import com.example.thuchiapp.views.customs.AnimationUtils
import com.tapadoo.alerter.Alerter
import java.text.DecimalFormat

class PendingJarsStartActivity : AppCompatActivity(),
    KeyBoardBottomSheetFragment.OnCalculationCompleteListener, OnAmountChangeListener {

    private lateinit var binding: ActivityPendingJarsStartBinding
    private lateinit var adapter: MoneySpinnerAdapter
    private var total: String = ""
    private lateinit var pendingJarsAmountViewModel: PendingJarsAmountViewModel
    private val listPendingJars: MutableList<PendingJars> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var language: String

    private val pendingJarsViewModel: PendingJarsViewModel by viewModels {
        PendingJarsViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingJarsStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewHome.visibility = View.GONE

        pendingJarsAmountViewModel = ViewModelProvider(this)[PendingJarsAmountViewModel::class.java]

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        language = sharedPreferences.getString("AppLanguage", "vi") ?: "vi"

        setupLottieAnimation()
        setupSpinner()
        setupLayout()
        getDataSource()
    }

    private fun setRecyclerView(totalWithoutDots: String) {
        val recyclerView = binding.recyclerViewHome
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        pendingJarsAmountViewModel.setPendingJars(listPendingJars)
        pendingJarsAmountViewModel.updateAmounts(totalWithoutDots.toDouble())

        pendingJarsAmountViewModel.allPendingJars.observe(this) { updatedPendingJars ->
            updatedPendingJars?.let {
                listPendingJars.addAll(it)
                val adapter = ConsumerCategoryAdapter(
                    it,
                    totalWithoutDots,
                    language,
                    this,
                    pendingJarsAmountViewModel
                ) // Truyền thêm ViewModel
                recyclerView.adapter = adapter
            }
        }

    }

    private fun getDataSource() {
        pendingJarsViewModel.allPendingJars.observe(this) { pendingJars ->
            val spendingJars = pendingJars.filter { jar -> jar.type == "Spending" }
            listPendingJars.addAll(spendingJars)
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
            setDataToServer()
        }
    }

    private fun setDataToServer() {
        pendingJarsViewModel.updatePendingJars(listPendingJars).observe(this) { result ->
            if (result) {
                PreferencesHelper.saveTotalToPreferences(this, total)
                Alerter.create(this)
                    .setTitle("Thành công!")
                    .setText("Thêm thành công số dự chi tháng!")
                    .setBackgroundColorRes(R.color.teal_200)
                    .setIcon(R.drawable.ic_success_32)
                    .setDuration(600) // 1 giây
                    .setOnHideListener {
                        val intent = Intent(this@PendingJarsStartActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .show()

            } else {
                Alerter.create(this)
                    .setTitle("Thất bại!")
                    .setText("Thất bại trong quá trình lưu data xin thử lại!")
                    .setBackgroundColorRes(R.color.red)
                    .setIcon(R.drawable.ic_cloud_cross_30)
                    .setDuration(600)
                    .show()
            }
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

        binding.spinnerOnboarding.setSelection(if (language == "vi") 0 else 1)
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
            setRecyclerView(totalWithoutDots)
            if (binding.layoutSlogan.visibility == View.VISIBLE && binding.recyclerViewHome.visibility == View.GONE) {
                AnimationUtils.collapseLayout(binding.layoutSlogan)
                AnimationUtils.expandLayout(binding.recyclerViewHome)
            }
        }
    }

    override fun onAmountUpdated(
        position: Int,
        percent: Int,
        newAmount: String,
        changePart: Double
    ) {
        listPendingJars[position].percent = percent
        listPendingJars[position].amount = newAmount
        val totalWithoutDots = total.replace(".", "")
        val currentTotal = totalWithoutDots.toDoubleOrNull() ?: 0.0
        val newTotal = currentTotal + changePart
        val formatter = DecimalFormat("#,###")
        total = formatter.format(newTotal)
        binding.estimatedAmount.text = total
    }

}
