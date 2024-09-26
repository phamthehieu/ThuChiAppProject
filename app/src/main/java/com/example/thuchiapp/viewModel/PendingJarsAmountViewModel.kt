package com.example.thuchiapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thuchiapp.entity.PendingJars

class PendingJarsAmountViewModel : ViewModel() {
    private val _allPendingJars = MutableLiveData<List<PendingJars>?>()
    val allPendingJars: MutableLiveData<List<PendingJars>?> = _allPendingJars

    // Hàm tính toán và cập nhật amount
    fun updateAmounts(total: Double) {
        val updatedPendingJars = _allPendingJars.value?.map { jar ->
            val percentValue = jar.percent ?: 0  // Nếu percent là null thì gán 0
            val calculatedAmount = (total * percentValue / 100).toString()  // Tính toán với percent
            jar.copy(amount = calculatedAmount)  // Tạo bản sao của PendingJar với amount mới
        }
        _allPendingJars.value = updatedPendingJars  // Cập nhật lại giá trị của LiveData
    }

    // Set danh sách PendingJars
    fun setPendingJars(pendingJars: List<PendingJars>) {
        _allPendingJars.value = pendingJars
    }
}
