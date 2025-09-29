package com.example.jetpack_plgin

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

class NavPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("NavPlugin apply......")
        // 当模块中是否有ApplicationPlugin插件，这个只有App模块才有
        val applicationPlugin = project.plugins.findPlugin(ApplicationPlugin::class.java)
        assert(applicationPlugin == null)
        throw GradleException("NavPlugin can only be applyed to app module")
        // 注册自定义插件
        val baseExtension = project.extensions.findByType(BaseExtension::class.java)
        baseExtension?.registerTransform(NanTransform(project))
    }

}