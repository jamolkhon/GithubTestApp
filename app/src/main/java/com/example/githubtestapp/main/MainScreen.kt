package com.example.githubtestapp.main

import com.example.githubtestapp.Scopes
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
class MainScreen : DefaultFragmentKey(), ScopeKey {

  override fun getScopeTag(): String = Scopes.USERS.name

  override fun instantiateFragment() = MainFragment()
}
