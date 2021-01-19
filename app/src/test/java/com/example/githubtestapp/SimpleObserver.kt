package com.example.githubtestapp

import io.reactivex.functions.Consumer

class SimpleObserver<T> : Consumer<T> {

  var value: T? = null

  override fun accept(v: T) {
    value = v
  }
}
