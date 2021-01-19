package com.example.githubtestapp.support

import android.content.res.Resources
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

fun RecyclerView.addOnReachBottomListener(listener: () -> Unit) {
  addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
          if (newState == RecyclerView.SCROLL_STATE_IDLE && !canScrollVertically(1)) {
              listener()
          }
      }
  })
}

fun Resources.dpToPx(value: Int): Int =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), displayMetrics)
    .roundToInt()
