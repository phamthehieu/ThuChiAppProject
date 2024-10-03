package com.example.thuchiapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thuchiapp.database.RevenueAndExpenditureDatabase
import com.example.thuchiapp.entity.PendingJars
import com.example.thuchiapp.repository.PendingJarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PendingJarsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PendingJarsRepository
    val allPendingJars: LiveData<List<PendingJars>>

    init {
        val pendingJarsDao = RevenueAndExpenditureDatabase.getDataBase(application, viewModelScope).pendingJarsDao()
        repository = PendingJarsRepository(pendingJarsDao)
        allPendingJars = repository.listPendingJars.asLiveData()
    }

    fun updatePendingJars(pendingJars: List<PendingJars>): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updatePendingJars(pendingJars)
                result.postValue(true)
            } catch (e: Exception) {
                result.postValue(false)
            }
        }
        return result
    }
}

class PendingJarsViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PendingJarsViewModel::class.java)) {
            return PendingJarsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}