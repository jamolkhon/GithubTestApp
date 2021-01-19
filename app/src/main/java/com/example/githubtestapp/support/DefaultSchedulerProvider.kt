package com.example.githubtestapp.support

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefaultSchedulerProvider @Inject constructor() : SchedulerProvider {

  override fun io(): Scheduler = Schedulers.io()

  override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}
