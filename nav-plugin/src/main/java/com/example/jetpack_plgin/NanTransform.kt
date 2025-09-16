package com.example.jetpack_plgin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.jetpack.plugin.runtime.NavData
import com.example.jetpack.plugin.runtime.NavDestination
import jdk.internal.org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.ZipFile

class NanTransform(project: Project) : Transform() {
    companion object {
        const val NAV_RUNTIME_DES = ""
        const val NAV_RUNTIME_NAV_TYPE = ""
    }

    private val navDatas = mutableListOf<NavData>()

    override fun getName(): String {
        // 获取插件名字
        return "NanTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT // 作用域
    }

    override fun isIncremental(): Boolean {
        return false //增量编译
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        val inputs = transformInvocation?.inputs ?: return
        val outputProvider = transformInvocation.outputProvider
        inputs.forEach {
            it.directoryInputs.forEach {
                handleDirectoryClass(it.file)
                val outPutDir = outputProvider.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.DIRECTORY
                )
                if (it.file.isFile) {
                    FileUtils.copyFile(it.file, outPutDir)
                } else {
                    FileUtils.copyDirectory(it.file, outPutDir)
                }

            }

            it.jarInputs.forEach {
                handleJarClasses(it.file)
                val outPutDir = outputProvider.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(it.file, outPutDir)

                if (it.file.isFile) {
                }
            }
        }
    }

    private fun handleJarClasses(file: File) {
        val zipFile = ZipFile(file)
        zipFile.stream().forEach {
            if (it.name.endsWith("class")) {
                val inputStream = zipFile.getInputStream(it)
                visitClass(inputStream)
                inputStream.close()
            }
        }
        zipFile.close()
    }

    private fun handleDirectoryClass(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                handleDirectoryClass(it)
            }
        } else if (file.extension.endsWith("class", true)) {
            val fileInputStream = FileInputStream(file)
            visitClass(fileInputStream)
            fileInputStream.close()
        }
    }


    private fun visitClass(inputStream: InputStream) {
        val classReader = ClassReader(inputStream)
        val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                //return super.visitAnnotation(descriptor, visible)
                if (descriptor != NAV_RUNTIME_DES) {
                    return object : AnnotationVisitor(Opcodes.ASM9, "") {

                    }
                }
                val annotationVisitor = object : AnnotationNode(Opcodes.ASM9, "") {

                    var route = ""
                    var type = NavDestination.NavType.None
                    override fun visit(name: String?, value: Any?) {
                        super.visit(name, value)
                        if (name == "route"){
                            route = value as String
                        }
                    }


                    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
                        super.visitEnum(name, descriptor, value)
                        if (name == "type"){
                            assert(value == null){

                            }
                            type = NavDestination.NavType.valueOf(value!!)
                        }
                    }

                    override fun visitEnd() {
                        super.visitEnd()
                        val navData = NavData(route,classReader.className.replace())
                        navDatas.add(navData)
                    }

                }

                return annotationVisitor

            }


        }
        classReader.accept(classVisitor, EXPAND_FRAMES)
    }
}
