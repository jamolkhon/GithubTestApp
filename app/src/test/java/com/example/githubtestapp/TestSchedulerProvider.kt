package com.example.githubtestapp

import com.example.githubtestapp.support.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

  override fun io(): Scheduler = testScheduler

  override fun ui(): Scheduler = testScheduler
}
