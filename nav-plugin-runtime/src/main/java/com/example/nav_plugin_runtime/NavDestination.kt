package com.example.nav_plugin_runtime

@Retention(AnnotationRetention.RUNTIME) // 作用域
@Target(AnnotationTarget.CLASS) // 标记域
annotation class NavDestination(val type: NavType, val name: String) {

}