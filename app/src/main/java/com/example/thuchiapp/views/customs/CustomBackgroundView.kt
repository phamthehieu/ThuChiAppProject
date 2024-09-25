package com.example.thuchiapp.views.customs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.thuchiapp.R

class CustomBackgroundView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green_header_home)  // Màu sắc tuỳ chỉnh
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Tạo đường cong như hình bạn cung cấp
        val path = Path().apply {
            moveTo(0f, height * 0.9f) // Bắt đầu từ 30% chiều cao
            quadTo(width / 2, height, width, height * 0.9f) // Tạo đường cong phía trên
            lineTo(width, 0f) // Kẻ đường từ phải về trên cùng
            lineTo(0f, 0f) // Kẻ đường từ trên trái về gốc
            close()
        }

        // Vẽ path với màu đã thiết lập
        canvas.drawPath(path, paint)
    }
}