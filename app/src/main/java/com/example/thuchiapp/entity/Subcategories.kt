package com.example.thuchiapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subcategories")
data class Subcategories (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subcategory: String,
    val order: Int,
    val categoryCode: Int,
)