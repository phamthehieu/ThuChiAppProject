package com.example.thuchiapp.views.customs

import android.animation.ValueAnimator
import android.view.View
import androidx.core.animation.doOnEnd

object AnimationUtils {
    // Hàm ẩn layout bằng cách giảm dần chiều cao
    fun collapseLayout(layout: View) {
        // Lấy chiều cao ban đầu của layout
        val initialHeight = layout.measuredHeight

        // Sử dụng ValueAnimator để thay đổi chiều cao từ chiều cao hiện tại về 0
        val animator = ValueAnimator.ofInt(initialHeight, 0)
        animator.addUpdateListener { valueAnimator ->
            // Cập nhật chiều cao của layout trong mỗi bước
            val animatedValue = valueAnimator.animatedValue as Int
            layout.layoutParams.height = animatedValue
            layout.requestLayout() // Cập nhật layout để thay đổi được áp dụng
        }

        animator.duration = 300 // Thời gian hiệu ứng
        animator.start()

        // Sau khi hoàn tất quá trình thu nhỏ, set visibility = GONE
        animator.doOnEnd {
            layout.visibility = View.GONE
        }
    }

    // Hàm hiện layout bằng cách tăng dần chiều cao
    fun expandLayout(layout: View) {
        // Đảm bảo layout đang bị ẩn
        layout.visibility = View.VISIBLE

        // Đo chiều cao thực tế mà layout sẽ cần
        val widthSpec = View.MeasureSpec.makeMeasureSpec(layout.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        layout.measure(widthSpec, heightSpec)
        val targetHeight = layout.measuredHeight

        // Đặt chiều cao của layout bắt đầu từ 0
        layout.layoutParams.height = 0
        layout.requestLayout()

        // Sử dụng ValueAnimator để tăng dần chiều cao từ 0 lên targetHeight
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.addUpdateListener { valueAnimator ->
            // Cập nhật chiều cao của layout trong mỗi bước
            val animatedValue = valueAnimator.animatedValue as Int
            layout.layoutParams.height = animatedValue
            layout.requestLayout()
        }

        animator.duration = 300 // Thời gian hiệu ứng
        animator.start()
    }
}