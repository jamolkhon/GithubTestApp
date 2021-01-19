package com.example.githubtestapp

import com.example.githubtestapp.detail.DetailScreen
import com.zhuinden.simplestack.Backstack

class Nav(private val backstack: Backstack) {

  fun goToDetailScreen(userId: Long) {
    backstack.goTo(DetailScreen(userId))
  }
}
