package com.example.nav_plugin


import com.example.nav_plugin.NavPluginContent.NAV_RUNTIME_DESTINATION
import com.example.nav_plugin.NavPluginContent.NAV_RUNTIME_MODULE_NAME
import com.example.nav_plugin.NavPluginContent.NAV_RUNTIME_PKG_NAME
import com.example.nav_plugin.NavPluginContent.NAV_RUNTIME_REGISTRY_CLASS_NAME
import com.example.nav_plugin_runtime.NavType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import java.io.File
import java.util.jar.JarFile

abstract class NavTransformTask : DefaultTask() {
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @get:InputFiles
    abstract val allDire: ListProperty<Directory>

    @get:OutputFile
    abstract val outputJar: RegularFileProperty

    @get:OutputFile
    abstract val mappingFile: RegularFileProperty

    private val navDatas = mutableListOf<NavData>()

    @TaskAction
    fun transform() {
        print("==================== NavTransformTask Running ====================")
        val result = mutableListOf<String>()

        // 遍历工程内所有的Jar
        allJars.get().forEach { jarFile ->
            processJarFile(jarFile.asFile, result)
        }
        // 遍历工程内又有的class
        allDire.get().forEach { classFile ->
            processClassFile(classFile.asFile, result)
        }
        generateNavNavRegistry(navDatas)
        val outFile = outputJar.get().asFile
        val mapFile = mappingFile.get().asFile
        print("====================================================================")
        outFile.parentFile.mkdirs()
        // 写出一个空的 jar，避免 FileNotFound
        java.util.jar.JarOutputStream(outFile.outputStream()).use { jar ->
            jar.putNextEntry(java.util.jar.JarEntry("META-INF/"))
            jar.closeEntry()
        }

        mapFile.parentFile.mkdirs()
        mapFile.writeText("Transform executed for ${project.name}")
    }

    /**
     * 利用kotlin Poet 生成一个类，放在Nav-plugin-runtime 模块，用于缓存注解所有标记的类
     *
     * object NavRegistry {
     *
     *     private val navList: ArrayList<NavData> = ArrayList()
     *
     *     init {
     *         navList.add(NavData("home_fragmant", "com.example.jetpack", NavType.Fragment))
     *         navList.add(NavData("home_fragmant", "com.example.jetpack", NavType.Fragment))
     *         navList.add(NavData("home_fragmant", "com.example.jetpack", NavType.Fragment))
     *     }
     *
     *     open fun get(): List<NavData> {
     *         var list = ArrayList<NavData>()
     *         list.addAll(navList)
     *         return list
     *     }
     * }
     *
     */
    fun generateNavNavRegistry(navDatas: MutableList<NavData>) {
        // 1. 生成List
        val navListProperty = PropertySpec.builder(
            "navList", // 成员变量名
            ClassName("kotlin.collections", "ArrayList").parameterizedBy(
                ClassName("", "NavData")
            )
        ).addModifiers(KModifier.PRIVATE)
            // .initializer("ArrayList()")
            .initializer(CodeBlock.builder().addStatement("ArrayList<NavData>()").build())
            .build()

        // 生成init 代码块
        val initBlock = CodeBlock.builder().apply {
            navDatas.forEach {
                addStatement(
                    "navList.add(NavData(\"%S\",\"%S\",NavType.%L))",
                    it.className,
                    it.route,
                    it.type
                )
            }
        }.build()

        // 生成get函数
        val getFun = FunSpec.builder("get")
            .addModifiers(KModifier.OPEN)
            .returns(
                ClassName("kotlin.collections", "List")
                    .parameterizedBy(ClassName("", "NavData"))
            )
            .addStatement("var list = ArrayList<NavData>()")
            .addStatement("list.addAll(navList)")
            .addStatement("return list")
            .build()

        // 生成Object NavRegistry 类
        val navRegistry = TypeSpec.objectBuilder("NavRegistry")
            .addProperty(navListProperty) // 添加成员属性
            .addInitializerBlock(initBlock) // 添加 init 代码块
            .addFunction(getFun) // 添加方法
            .build()

        // 生成 kt文件
        val fileSpec = FileSpec.builder(NAV_RUNTIME_PKG_NAME, NAV_RUNTIME_REGISTRY_CLASS_NAME)
            .addType(navRegistry)
            .addImport(NavType::class.java, "Fragment", "Dialog", "Activity")
            .build()

        // 写入文件
        val navProject = project.rootProject.findProject(NAV_RUNTIME_MODULE_NAME)
        assert(navProject == null) {
            throw GradleException("can not find $NAV_RUNTIME_MODULE_NAME")
        }
        val sourceSets = navProject!!.extensions.findByName("sourceSets") as SourceSetContainer
        val outPutFile = sourceSets.first().java.srcDirs.first().absoluteFile
        fileSpec.writeTo(outPutFile)

    }

    fun processClassFile(
        classFile: File,
        result: MutableList<String>
    ) {
        classFile.inputStream().use { input ->
            val classReader = ClassReader(input)
            val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
                var className: String? = null
                override fun visit(
                    version: Int,
                    access: Int,
                    name: String?,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<out String?>?
                ) {
                    print("---------> processClassFile name = ${name}")
                    className = name
                }

                override fun visitAnnotation(
                    descriptor: String?,
                    visible: Boolean
                ): AnnotationVisitor? {
                    // 不是我们自定义的注解，返回一个空的注解处理
                    if (descriptor != NavPluginContent.NAV_RUNTIME_DESTINATION) {
                        return object : AnnotationNode(Opcodes.ASM9, "") {

                        }
                    }
                    val annotationVisitor = object : AnnotationNode(Opcodes.ASM9, "") {
                        var route: String = ""
                        var type: NavType = NavType.None

                        // 注解中是基本数据类型的时候 回调这里
                        override fun visit(name: String?, value: Any?) {
                            super.visit(name, value)
                            if (name == NavPluginContent.KEY_ROUTE) {
                                route = value as String
                            }
                        }

                        override fun visitAnnotation(
                            name: String?,
                            descriptor: String?
                        ): AnnotationVisitor? {
                            return super.visitAnnotation(name, descriptor)
                        }

                        // 注解中是数组的时候 回调这里
                        override fun visitArray(name: String?): AnnotationVisitor? {
                            return super.visitArray(name)
                        }

                        // 注解中是枚举的时候 回调这里
                        override fun visitEnum(name: String?, descriptor: String?, value: String?) {
                            super.visitEnum(name, descriptor, value)
                            if (name == NavPluginContent.KEY_TYPE) {
                                assert(value == null) {
                                    throw GradleException("key type can not be null !!!")
                                }
                                type = NavType.valueOf(value!!)
                            }

                        }

                        override fun visitEnd() {
                            super.visitEnd()
                            // 生成NavData 对象
                        }
                    }
                    result.add("Found @MyRoute on class: ${className?.replace('/', '.')}")

                    return annotationVisitor
                }
            }

            classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
        }
    }

    fun processJarFile(
        file: File,
        result: MutableList<String>
    ) {
        JarFile(file).use { jarFile ->
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                print("---------> processJarFile entry = ${entry.name}")
                if (entry.name.endsWith(".class")) {
                    jarFile.getInputStream(entry).use { input ->
                        val classReader = ClassReader(input)
                        val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
                            var className: String? = null

                            override fun visit(
                                version: Int,
                                access: Int,
                                name: String?,
                                signature: String?,
                                superName: String?,
                                interfaces: Array<out String?>?
                            ) {
                                print("---------> visit class = ${name}")
                                className = name
                            }

                            override fun visitAnnotation(
                                descriptor: String?,
                                visible: Boolean
                            ): AnnotationVisitor? {
                                if (descriptor == NAV_RUNTIME_DESTINATION) {
                                    result.add(
                                        "Found @MyRoute on class: ${
                                            className?.replace(
                                                '/',
                                                '.'
                                            )
                                        }"
                                    )
                                }
                                return super.visitAnnotation(descriptor, visible)
                            }
                        }
                        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
                    }
                }
            }

        }
    }
}