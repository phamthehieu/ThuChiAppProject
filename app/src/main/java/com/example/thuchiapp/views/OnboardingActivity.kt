package com.example.thuchiapp.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.thuchiapp.MainActivity
import com.example.thuchiapp.R
import com.example.thuchiapp.adapter.IconNameSpinnerAdapter
import com.example.thuchiapp.data.IconNameItem
import com.example.thuchiapp.databinding.ActivityOnboardingBinding
import com.example.thuchiapp.viewModel.PendingJarsViewModel
import com.example.thuchiapp.viewModel.PendingJarsViewModelFactory
import com.example.thuchiapp.views.splash.PendingJarsStartActivity
import com.example.thuchiapp.views.user.LoginWithGoogleActivity
import com.tapadoo.alerter.Alerter
import java.util.Locale

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var items: List<IconNameItem>
    private lateinit var adapter: IconNameSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        val language = sharedPreferences.getString("AppLanguage", "vi") ?: "vi"
        setLocale(language)

        binding.spinnerOnboarding.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> setLocale("vi")
                        1 -> setLocale("en")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        setupSpinner()

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, LoginWithGoogleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupSpinner() {
        // Tạo danh sách items cho Spinner
        items = listOf(
            IconNameItem(R.drawable.ic_vietnam_30, R.string.viet_nam),
            IconNameItem(R.drawable.ic_english_30, R.string.english),
        )
        // Thiết lập adapter cho Spinner (chỉ khởi tạo 1 lần)
        if (!::adapter.isInitialized) {
            adapter = IconNameSpinnerAdapter(this, items)
            binding.spinnerOnboarding.adapter = adapter
        } else {
            // Cập nhật lại dữ liệu cho adapter sau khi thay đổi ngôn ngữ
            adapter.notifyDataSetChanged()
        }
        // Cập nhật lựa chọn hiện tại sau khi thay đổi ngôn ngữ
        val selectedPosition = binding.spinnerOnboarding.selectedItemPosition
        binding.spinnerOnboarding.setSelection(selectedPosition)
    }

    private fun setLocale(languageCode: String) {
        // Lưu ngôn ngữ mới vào SharedPreferences
        with(sharedPreferences.edit()) {
            putString("AppLanguage", languageCode)
            apply()
        }
        // Cập nhật Locale của ứng dụng
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        // Cập nhật UI sau khi thay đổi ngôn ngữ
        updateUI()
    }

    private fun updateUI() {
        // Cập nhật text cho các View khác
        binding.slogan.text = String.format(Locale.getDefault(), getString(R.string.slogan))
        binding.textBtn.text = String.format(Locale.getDefault(), getString(R.string.get_started))
        // Cập nhật lại Spinner để hiển thị ngôn ngữ mới
        setupSpinner()
    }
}
