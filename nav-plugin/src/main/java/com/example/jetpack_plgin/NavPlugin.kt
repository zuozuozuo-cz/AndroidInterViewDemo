// NavPlugin.kt
package com.example.jetpack_plugin.navplugin

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.DefaultTask


import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


class NavPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("NavPlugin apply......")
        println("NavPlugin applied...")

        // 遍历 App 模块中的 variants（Groovy build.gradle 里可以传参数）
        project.afterEvaluate {
            val android = project.extensions.findByName("android")
            if (android == null) {
                println("Not an Android module, NavPlugin skipped")
                return@afterEvaluate
            }

            val buildTypes =
                android::class.java.getMethod("getBuildTypes").invoke(android) as Map<*, *>
            buildTypes.keys.forEach { buildTypeName ->
                val taskName = "navInjectTask${buildTypeName.toString().capitalize()}"
                val taskProvider = project.tasks.register(taskName, NavInjectTask::class.java)
                taskProvider.configure { task ->
                    task.variantName = buildTypeName.toString()
                    task.classesDir =
                        File(project.buildDir, "intermediates/javac/${buildTypeName}/classes")
                }

            }

        }
    }

}

abstract class NavInjectTask : DefaultTask() {
    lateinit var variantName: String
    lateinit var classesDir: File
    
}
