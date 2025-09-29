package com.example.jetpack_plgin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.example.jetpack.plugin.runtime.NavData
import com.example.jetpack.plugin.runtime.NavDestination
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import jdk.internal.org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
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
import com.android.build.gradle.internal.pipeline.TransformManager


class NanTransform(val project: Project) : Transform() {
    companion object {
        const val NAV_RUNTIME_DESTINATION = "Lcom/com/example/jetpack/plugin/runtime/NavDestination"
        const val NAV_RUNTIME_NAV_TYPE =
            "Lcom/com/example/jetpack/plugin/runtime/NavDestination\$NavType"
        private const val KEY_ROUTE = "route"
        private const val KEY_TYPE = "type"
        private const val NAV_RUNTIME_PKG_NAME = "com.example.jetpack_plgin.runtime"
        private const val NAV_RUNTIME_REGISTRY_CLASS_NAME = "NavRegistry"
        private const val NAV_RUNTIME_NAV_DATA_CLASS_NAME = "NavData"
        private const val NAV_RUNTIME_NAV_LIST = "NavList"
        private const val NAV_RUNTIME_MODULE_NAME = "nav-plugin-runtime"
    }

    private val navDatas = mutableListOf<NavData>()

    override fun getName(): String {
        // 获取插件名字
        return "NavTransform"
    }

//    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
//        return TransformManager.SCOPE_FULL_PROJECT // 作用域
//    }
    

    override fun isIncremental(): Boolean {
        return false //增量编译
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        // 获取输入
        val inputs = transformInvocation?.inputs ?: return
        val outputProvider = transformInvocation.outputProvider
        // 遍历所有 jar 或者 zip文件，提取我们的注解代码信息
        inputs.forEach {
            // 源码
            it.directoryInputs.forEach {
                handleDirectoryClass(it.file)
                // 输出目录，下一个Transform会从这里获取内容，从而传递下去
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
            // jar
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
        // 提取完成后，生成NavRegistry 类
        generateNavRegistry()
    }

    /**
     * 通过kotlinpoet 生成NavRegistry.kt文件，存放在nav-plugin-runtime模块下
     * 用于存放项目中所有的路由节点数据，用于灵活跳转
     */
    private fun generateNavRegistry() {
        // 生成成员变量  泛型为navData 的 ArrayList
        val navDataClassName = ClassName(NAV_RUNTIME_PKG_NAME, NAV_RUNTIME_REGISTRY_CLASS_NAME)
        val arrayListClassName = ClassName("kotlin.collections", "ArrayList")

        // 生成get方法的返回值
        val list = ClassName("kotlin.collections", "list")
        val arrayListOfNavData = arrayListClassName.parameterizedBy(navDataClassName)
        val listOfNavData = arrayListClassName.parameterizedBy(list)

        // init 代码块的代码内容，将采集到的navData 添加到 一个List集合中
        val stringBuilder = StringBuilder()
        navDatas.forEach {
            stringBuilder.append(
                "navList.add(NavData(\"%s\",\"%s\",%s))", it.route, it.className, it.type
            )
            stringBuilder.append("\n")
        }

        // 生成 Object class 的成员变量，一个范行为 NavData 的 ArrayList
        val property =
            PropertySpec.builder(NAV_RUNTIME_NAV_LIST, arrayListOfNavData, KModifier.PRIVATE)
                .initializer(CodeBlock.builder().addStatement("ArrayList<NavData>").build()).build()

        // 生成一个 Object 的单例类
        val typeSpec = TypeSpec.objectBuilder(NAV_RUNTIME_REGISTRY_CLASS_NAME) // 类名
            .addProperty(property) // 成员变量
            .addInitializerBlock( // 增加init 代码块
                CodeBlock.builder().addStatement(stringBuilder.toString()).build()
            )
            .addFunction(// 方法
                FunSpec.builder("get").returns(listOfNavData).addCode(
                    CodeBlock.builder()
                        .addStatement("val list = ArrayList<NavData> \n list.addAll(navList) \n return list")
                        .build()
                ).build()
            ).build()

        //1、生成文件
        val fileSpecBuilder =
            FileSpec.builder(NAV_RUNTIME_PKG_NAME, NAV_RUNTIME_REGISTRY_CLASS_NAME)
                .addComment("this file is generate by auto")
                .addType(typeSpec)
                .addImport(
                    NavDestination.NavType::class.java,
                    "Fragment",
                    "Dialog",
                    "Activity"
                ).build() // 增加导包

        val navRuntime = project.rootProject.findProject(NAV_RUNTIME_MODULE_NAME)
        assert(navRuntime == null) {
            throw GradleException("can not find $NAV_RUNTIME_MODULE_NAME")
        }

        val sourceSets = navRuntime!!.extensions.findByName("sourceSets") as SourceSetContainer
        val absoluteFile = sourceSets.first().java.srcDirs.first().absoluteFile
        fileSpecBuilder.writeTo(absoluteFile)

    }

    private fun handleJarClasses(file: File) {
        // 解压
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
        // 如果是目录，递归
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                handleDirectoryClass(it)
            }
            // 只处理 class
        } else if (file.extension.endsWith("class", true)) {
            val fileInputStream = FileInputStream(file)
            visitClass(fileInputStream)
            fileInputStream.close()
        }
    }

    /**
     * 解析class文件
     */
    private fun visitClass(inputStream: InputStream) {
        // class 文件读取器
        val classReader = ClassReader(inputStream)
        // class 读取回调
        val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                /**
                 * descriptor 当前访问的注解名字
                 *  这里的名字需要和NavDestination的字节码对其
                 */
                if (descriptor != NAV_RUNTIME_DESTINATION) {
                    return object : AnnotationVisitor(Opcodes.ASM9) {

                    }
                }
                // 定义注解读取器
                val annotationVisitor = object : AnnotationNode(Opcodes.ASM9, "") {
                    var route = ""
                    var type = NavDestination.NavType.None

                    // 基本数据类型
                    override fun visit(name: String?, value: Any?) {
                        super.visit(name, value)
                        // 拿到路径
                        if (name == KEY_ROUTE) {
                            route = value as String
                        }
                    }

                    // 枚举
                    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
                        super.visitEnum(name, descriptor, value)
                        //拿到Type
                        if (name == KEY_TYPE) {
                            assert(value == null) {
                                throw GradleException("$type 不对")
                            }
                            type = NavDestination.NavType.valueOf(value!!)
                        }
                    }

                    // 数组
                    override fun visitArray(name: String?): AnnotationVisitor? {
                        return super.visitArray(name)
                    }

                    //注解读取完成
                    override fun visitEnd() {
                        super.visitEnd()
                        // 获取完成，构建一个NavData对象
                        val navData = NavData(classReader.className.replace("/", "."), route, type)
                        navDatas.add(navData)
                    }

                }

                return annotationVisitor

            }


        }
        classReader.accept(classVisitor, EXPAND_FRAMES)
    }
}
