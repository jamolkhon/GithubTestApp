package com.example.githubtestapp

sealed class Event {

  object ConnectionProblem : Event()

  object UnknownProblem : Event()
}
