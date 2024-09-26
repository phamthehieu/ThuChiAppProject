package com.example.thuchiapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_jars")
data class PendingJars(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: String,
    val iconResource: Int,
    val type: String,
    val percent: Int
    )
