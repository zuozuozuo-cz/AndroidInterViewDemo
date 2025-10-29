package com.example.nav_plugin

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized

class NavPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withId("com.android.application") {
            project.extensions.configure(ApplicationAndroidComponentsExtension::class.java) { extention ->
                extention.onVariants {
                    val taskProvider = project.tasks.register(
                        "transform_${it.name.capitalized()}",
                        NavTransformTask::class.java
                    ) {
                        // 指定 mapping 文件输出路径
                            task ->
                        task.mappingFile.set(project.buildDir.resolve("outputs/transform/mapping.txt"))

                    }
                    it.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                        .use(taskProvider)
                        .toTransform(
                            ScopedArtifact.CLASSES,
                            NavTransformTask::allJars,
                            NavTransformTask::allDire,
                            NavTransformTask::outputJar
                        )
                }
            }
        }
    }
}