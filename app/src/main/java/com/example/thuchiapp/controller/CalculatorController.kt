package com.example.thuchiapp.controller

import com.example.thuchiapp.R
import com.example.thuchiapp.model.CalculationModel
import com.example.thuchiapp.views.component.KeyBoardBottomSheetFragment
import java.math.BigDecimal
import java.text.DecimalFormat

class CalculationController(private val fragment: KeyBoardBottomSheetFragment) {

    private val model = CalculationModel()

    fun updateTotal(totalFromActivity: String?) {
        if (totalFromActivity != null) {
            model.numberSequence.append(totalFromActivity)
        }
    }

    // Khi người dùng nhấn các nút trên bàn phím
    fun onNumberButtonClick(viewId: Int, updateView: (String) -> Unit) {
        val numberToAdd = when (viewId) {
            R.id.buttonZero -> "0"
            R.id.buttonOne -> "1"
            R.id.buttonTwo -> "2"
            R.id.buttonThree -> "3"
            R.id.buttonFour -> "4"
            R.id.buttonFive -> "5"
            R.id.buttonSix -> "6"
            R.id.buttonSeven -> "7"
            R.id.buttonEight -> "8"
            R.id.buttonNine -> "9"
            R.id.buttonComma -> "."
            R.id.buttonThreeZero -> "000"
            R.id.buttonKeyBoardDelete -> {
                handleDeleteAction()
                ""
            }

            R.id.buttonPlus -> {
                handleOperator("+", updateView)
                ""
            }

            R.id.buttonMinus -> {
                handleOperator("-", updateView)
                ""
            }

            R.id.successKeyBoardBtn -> {
                if (model.numberSequence.isEmpty() && model.calculationMark === "" || model.numberSequence2.isEmpty() && model.calculationMark === "") {
                    fragment.dismissKeyboardFragment()
                }
                doCalculation(updateView)
                ""
            }

            else -> ""
        }

        if (numberToAdd.isNotEmpty()) {
            model.numberSequence.append(numberToAdd)
        }

        val displayText = buildExpression()
        updateView(displayText)
    }

    // Xử lý khi người dùng nhập toán tử (+ hoặc -)
    private fun handleOperator(operator: String, updateView: (String) -> Unit) {
        if (model.numberSequence.isNotEmpty()) {
            model.numberSequence2.append(model.numberSequence)
            model.numberSequence.clear()
        }
        model.calculationMark = " $operator "
        updateView(buildExpression()) // Cập nhật biểu thức ngay sau khi bấm toán tử
    }

    // Xử lý khi người dùng xóa
    private fun handleDeleteAction() {
        if (model.numberSequence.isNotEmpty()) {
            model.numberSequence.deleteCharAt(model.numberSequence.length - 1)
        } else if (model.calculationMark.isNotEmpty()) {
            model.calculationMark = ""
        } else if (model.numberSequence2.isNotEmpty()) {
            model.numberSequence2.deleteCharAt(model.numberSequence2.length - 1)
        }
    }

    // Xử lý khi bấm nút "Done" để thực hiện phép tính
    private fun doCalculation(updateView: (String) -> Unit) {
        val result = model.calculate()
        if (result != null) {
            model.numberSequence.clear()
            model.numberSequence.append(result.toString())
            model.numberSequence2.clear()
            model.calculationMark = ""
            updateView(formatNumberWithDots(result.toString()))
        }
    }

    // Hàm xây dựng biểu thức để hiển thị (gồm số + toán tử + số)
    private fun buildExpression(): String {
        val part1 = formatNumberWithDots(model.numberSequence2.toString())
        val part2 = formatNumberWithDots(model.numberSequence.toString())
        return if (model.calculationMark.isEmpty()) {
            part2
        } else {
            "$part1${model.calculationMark}$part2"
        }
    }

    // Hàm format số có dấu chấm ngăn cách phần nghìn
    private fun formatNumberWithDots(number: String): String {
        val hasComma = number.contains('.')
        val cleanNumber = number.replace("[^0-9.]".toRegex(), "")
        if (cleanNumber.isEmpty()) return ""

        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(BigDecimal(cleanNumber))

        return if (hasComma) "$formattedNumber." else formattedNumber
    }
}
