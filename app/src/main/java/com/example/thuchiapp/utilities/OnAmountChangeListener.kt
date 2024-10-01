package com.example.thuchiapp.utilities

interface OnAmountChangeListener {
    fun     onAmountUpdated(position: Int, percent: Int, newAmount: String, changePart: Double)
}
