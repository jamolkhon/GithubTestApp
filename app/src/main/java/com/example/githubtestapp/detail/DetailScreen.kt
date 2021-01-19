package com.example.githubtestapp.detail

import com.example.githubtestapp.Scopes
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailScreen(val userId: Long) : DefaultFragmentKey(), ScopeKey {

  override fun getScopeTag(): String = Scopes.USERS.name

  override fun instantiateFragment() = DetailFragment()
}
