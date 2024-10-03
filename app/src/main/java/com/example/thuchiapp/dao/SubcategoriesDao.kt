package com.example.thuchiapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thuchiapp.entity.Subcategories
import kotlinx.coroutines.flow.Flow

@Dao
interface SubcategoriesDao {

    @Query("SELECT * FROM subcategories")
    fun getAllSubcategories(): Flow<List<Subcategories>>

    @Query("SELECT * FROM subcategories WHERE categoryCode = :categoryCode")
    fun getSubcategoriesByCategoryCode(categoryCode: Int): Flow<List<Subcategories>>

    @Insert
    fun insertSubcategories(subcategories: List<Subcategories>)

}