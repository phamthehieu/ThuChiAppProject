package com.example.thuchiapp.repository

import com.example.thuchiapp.dao.SubcategoriesDao
import com.example.thuchiapp.entity.Subcategories
import kotlinx.coroutines.flow.Flow

class SubcategoriesRepository(private val subcategoriesDao: SubcategoriesDao) {
    val listSubcategories: Flow<List<Subcategories>> = subcategoriesDao.getAllSubcategories()

    fun getListSubcategoriesByCategoryCode(categoryCode: Int): Flow<List<Subcategories>> {
        return subcategoriesDao.getSubcategoriesByCategoryCode(categoryCode)
    }

    fun insertSubcategories(subcategories: List<Subcategories>) {
        subcategoriesDao.insertSubcategories(subcategories)

    }
}