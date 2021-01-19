package com.example.githubtestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.githubtestapp.R
import com.example.githubtestapp.main.MainScreen
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentStateChanger
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider

class MainActivity : AppCompatActivity(), SimpleStateChanger.NavigationHandler {

  private lateinit var fragmentStateChanger: DefaultFragmentStateChanger

  lateinit var nav: Nav

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fragmentStateChanger = DefaultFragmentStateChanger(supportFragmentManager, R.id.container)

    val appComponent = DaggerAppComponent.create()

    val backstack = Navigator.configure()
      .setScopedServices(ScopeConfiguration(appComponent))
      .setStateChanger(SimpleStateChanger(this))
      .install(this, findViewById(R.id.container), History.of(MainScreen()))
    nav = Nav(backstack)
  }

  override fun onBackPressed() {
    if (!Navigator.onBackPressed(this)) {
      super.onBackPressed()
    }
  }

  override fun onNavigationEvent(stateChange: StateChange) {
    fragmentStateChanger.handleStateChange(stateChange)
  }
}

val Fragment.nav: Nav
  get() = (requireActivity() as MainActivity).nav
