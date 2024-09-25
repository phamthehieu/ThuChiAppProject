package com.example.thuchiapp.views.component

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thuchiapp.R
import com.example.thuchiapp.controller.CalculationController
import com.example.thuchiapp.databinding.FragmentKeyBoardBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KeyBoardBottomSheetFragment : BottomSheetDialogFragment() {

    interface OnCalculationCompleteListener {
        fun onCalculationComplete(result: String)
        fun onBottomSheetDismissed()
    }

    private lateinit var binding: FragmentKeyBoardBottomSheetBinding
    private var listener: OnCalculationCompleteListener? = null
    private lateinit var controller: CalculationController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setDimAmount(0f)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet =
                (dialogInterface as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(resources.getColor(R.color.white, null))
        }
        return dialog
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCalculationCompleteListener) {
            listener = context
        }
        controller = CalculationController(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKeyBoardBottomSheetBinding.inflate(inflater, container, false)
        setupNumberButtons()

        val totalFromActivity = arguments?.getString("total")
        controller.updateTotal(totalFromActivity)

        return binding.root
    }

    private fun setupNumberButtons() {
        val buttonIds = listOf(
            binding.buttonZero, binding.buttonOne, binding.buttonTwo,
            binding.buttonThree, binding.buttonFour, binding.buttonFive,
            binding.buttonSix, binding.buttonSeven, binding.buttonEight,
            binding.buttonNine, binding.buttonPlus, binding.buttonMinus,
            binding.buttonComma, binding.buttonKeyBoardDelete,
            binding.successKeyBoardBtn, binding.buttonThreeZero
        )

        for (button in buttonIds) {
            button.setOnClickListener { onNumberButtonClick(it) }
        }
    }

    private fun onNumberButtonClick(view: View) {
        controller.onNumberButtonClick(view.id) { displayText ->
            listener?.onCalculationComplete(displayText)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onBottomSheetDismissed()
    }

    fun dismissKeyboardFragment() {
        dismiss()
    }
}