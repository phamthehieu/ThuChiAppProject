package com.example.thuchiapp.repository

import com.example.thuchiapp.dao.PendingJarsDao
import com.example.thuchiapp.entity.PendingJars
import kotlinx.coroutines.flow.Flow

class PendingJarsRepository(private val pendingJarsDao: PendingJarsDao) {
    val listPendingJars: Flow<List<PendingJars>> = pendingJarsDao.getAllPendingJars()

    fun updatePendingJars(pendingJars: List<PendingJars>) {
        pendingJarsDao.updatePendingJars(pendingJars)
    }

}