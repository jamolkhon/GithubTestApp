package com.example.githubtestapp.support

import io.reactivex.Scheduler

interface SchedulerProvider {

  fun io(): Scheduler

  fun ui(): Scheduler
}
