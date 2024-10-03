package com.example.thuchiapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thuchiapp.database.RevenueAndExpenditureDatabase
import com.example.thuchiapp.entity.Subcategories
import com.example.thuchiapp.repository.SubcategoriesRepository

class SubcategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SubcategoriesRepository
    private var listSubcategories: LiveData<List<Subcategories>>

    init {
        val subcategoriesDao = RevenueAndExpenditureDatabase.getDataBase(application, viewModelScope).subcategoriesDao()
        repository = SubcategoriesRepository(subcategoriesDao)
        listSubcategories = repository.listSubcategories.asLiveData()
    }

    fun getListSubcategoriesByCategoryCode(categoryCode: Int): LiveData<List<Subcategories>> {
        return repository.getListSubcategoriesByCategoryCode(categoryCode).asLiveData()
    }

}

class SubcategoriesViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubcategoriesViewModel::class.java)) {
            return SubcategoriesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}