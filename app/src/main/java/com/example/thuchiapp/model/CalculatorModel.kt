package com.example.thuchiapp.model

import java.math.BigDecimal

class CalculationModel {
    var total: BigDecimal = BigDecimal.ZERO
    var numberSequence = StringBuilder()
    var numberSequence2 = StringBuilder()
    var calculationMark: String = ""

    // Phương thức để xử lý tính toán
    fun calculate(): BigDecimal? {
        val number1 = numberSequence2.toString().replace(',', '.').toBigDecimalOrNull()
        val number2 = numberSequence.toString().replace(',', '.').toBigDecimalOrNull()

        return when (calculationMark) {
            " + " -> if (number1 != null && number2 != null) number1 + number2 else null
            " - " -> if (number1 != null && number2 != null) number1 - number2 else null
            else -> null
        }
    }

    fun clear() {
        numberSequence.clear()
        numberSequence2.clear()
        calculationMark = ""
        total = BigDecimal.ZERO
    }
}