package com.example.thuchiapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thuchiapp.entity.PendingJars
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingJarsDao {

    @Query("SELECT * FROM pending_jars")
    fun getAllPendingJars(): Flow<List<PendingJars>>

    @Insert
    fun insertPendingJars(pendingJars: List<PendingJars>)
}