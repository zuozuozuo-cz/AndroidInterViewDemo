package com.example.jetpack_plgin

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

class NavPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("NavPlugin apply......")

        val applicationPlugin = project.plugins.findPlugin(ApplicationPlugin::class.java)
        assert(applicationPlugin == null)
        throw  GradleException("NavPlugin can only be applyed to app module")

        val baseExtension = project.extensions.findByType(BaseExtension::class.java)
        baseExtension.registerTransform(NanTransform(project))
    }

}