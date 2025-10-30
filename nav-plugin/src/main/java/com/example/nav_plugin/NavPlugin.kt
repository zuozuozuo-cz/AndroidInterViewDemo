package com.example.nav_plugin

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized

/**
 * 插件主类：
 * 实现Plugin接口，自定义插件
 */
class NavPlugin : Plugin<Project> {

    //插件入口方法，当插件被项目使用的时候被调用
    override fun apply(project: Project) {

        // 只有项目使用了 com.android.application 插件才会触发回调
        project.plugins.withId("com.android.application") {
            // 通过project拿到 Android插件的扩展 进行配置
            project.extensions.configure(ApplicationAndroidComponentsExtension::class.java) { extention ->
                // 遍历变体
                extention.onVariants {
                    // 注册我自定义的任务，得到一个TaskProvider
                    val taskProvider = project.tasks.register(
                        "transform_${it.name.capitalized()}",
                        NavTransformTask::class.java
                    ) {
                        // 指定 mapping 文件输出路径
                            task ->
                        task.mappingFile.set(project.buildDir.resolve("outputs/transform/mapping.txt"))

                    }
                    /**
                     *  将上面的 TaskProvider 注入到当前这个变体中
                     * ScopedArtifacts.Scope 是当前任务的作用域：
                     *  -ALL 是指所有的jar包以及class 包括三方库的
                     *  -PROJECT 是指仅当前工程中的 jar以及class
                     */
                    it.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
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