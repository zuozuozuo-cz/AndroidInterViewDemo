package com.example.jetpack.plugin.runtime

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class NavDestination(val type: NavType, val route: String) {
    enum class NavType {
        Fragment, Activity, Dialog, None
    }
}