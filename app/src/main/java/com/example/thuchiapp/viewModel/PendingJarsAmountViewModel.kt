package com.example.thuchiapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thuchiapp.entity.PendingJars
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PendingJarsAmountViewModel : ViewModel() {
    private val _allPendingJars = MutableLiveData<List<PendingJars>?>()
    val allPendingJars: LiveData<List<PendingJars>?> get() = _allPendingJars

    // Hàm cập nhật danh sách với amount và percent mới
    fun updateAmounts(total: Double) {
        val updatedPendingJars = _allPendingJars.value?.map { jar ->
            val percentValue = jar.percent ?: 0
            val calculatedAmount = (total * percentValue / 100).toString()
            jar.copy(amount = calculatedAmount)
        }
        _allPendingJars.value = updatedPendingJars
    }

    // Đặt danh sách PendingJars
    fun setPendingJars(pendingJars: List<PendingJars>) {
        _allPendingJars.value = pendingJars
    }

    // Cập nhật percent và amount theo vị trí
    fun updatePercentAtPosition(position: Int, newAmount: Double) {
        val updatedPendingJars = _allPendingJars.value?.toMutableList()?.apply {
            val jar = this[position]
            this[position] = jar.copy(amount = newAmount.toString())
        }
        _allPendingJars.value = updatedPendingJars
    }
}

