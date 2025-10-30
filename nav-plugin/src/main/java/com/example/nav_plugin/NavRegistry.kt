package com.example.nav_plugin

import com.android.tools.r8.internal.ls
import com.example.nav_plugin_runtime.NavType

object NavRegistry {

    private val navList: ArrayList<NavData> = ArrayList()

    init {
        navList.add(NavData("home_fragmant", "com.example.jetpack", NavType.Fragment))
    }

    open fun get(): List<NavData> {
        var list = ArrayList<NavData>()
        list.addAll(navList)
        return list
    }
}