package com.example.githubtestapp.support

import com.zhuinden.eventemitter.EventEmitter
import io.reactivex.functions.Consumer

class EventEmitterConsumer<E : Any> : EventEmitter<E>(), Consumer<E> {

  override fun accept(e: E) {
    emit(e)
  }
}
