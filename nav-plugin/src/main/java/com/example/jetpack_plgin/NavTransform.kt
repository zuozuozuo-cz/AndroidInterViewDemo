import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.jetpack.plugin.runtime.NavData
import com.example.jetpack.plugin.runtime.NavDestination
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class NavTransform(val project: Project) : Transform() {
    companion object {
        const val NAV_RUNTIME_DESTINATION = "Lcom/example/jetpack/plugin/runtime/NavDestination;"
        const val NAV_RUNTIME_NAV_TYPE =
            "Lcom/example/jetpack/plugin/runtime/NavDestination\$NavType;"
        private const val KEY_ROUTE = "route"
        private const val KEY_TYPE = "type"
        private const val NAV_RUNTIME_PKG_NAME = "com.example.jetpack_plgin.runtime"
        private const val NAV_RUNTIME_REGISTRY_CLASS_NAME = "NavRegistry"
        private const val NAV_RUNTIME_NAV_DATA_CLASS_NAME = "NavData"
        private const val NAV_RUNTIME_NAV_LIST = "navList"
        private const val NAV_RUNTIME_MODULE_NAME = "nav-plugin-runtime"
    }

    private val navDatas = mutableListOf<NavData>()

    override fun getName(): String {
        return "NavTransform"
    }

    // 修复1: 使用正确的返回类型 - 使用 TransformManager.CONTENT_CLASS
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    // 修复2: 使用正确的返回类型 - 使用 TransformManager.SCOPE_FULL_PROJECT
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    // 修复3: 移除参数的可空标记，TransformInvocation 不会为 null
    override fun transform(transformInvocation: TransformInvocation) {
        // 获取输入
        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider

        // 遍历所有 jar 或者目录文件，提取注解代码信息
        inputs.forEach { transformInput ->
            // 处理目录输入
            transformInput.directoryInputs.forEach { directoryInput ->
                handleDirectoryClass(directoryInput.file)
                // 输出目录
                val outputDir = outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                if (directoryInput.file.isFile) {
                    FileUtils.copyFile(directoryInput.file, outputDir)
                } else {
                    FileUtils.copyDirectory(directoryInput.file, outputDir)
                }
            }

            // 处理 jar 输入
            transformInput.jarInputs.forEach { jarInput ->
                handleJarClasses(jarInput.file)
                val outputJar = outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(jarInput.file, outputJar)
            }
        }

        // 提取完成后，生成 NavRegistry 类
        generateNavRegistry()
    }

    /**
     * 生成 NavRegistry 类
     */
    private fun generateNavRegistry() {
        // 这里使用 ASM 生成类文件，而不是 KotlinPoet
        // 因为 KotlinPoet 需要额外依赖，且生成的是源码不是字节码

        if (navDatas.isEmpty()) {
            println("NavPlugin: No NavDestination annotations found")
            return
        }

        println("NavPlugin: Generating NavRegistry with ${navDatas.size} destinations")

        val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)

        // 定义类
        classWriter.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
            "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_REGISTRY_CLASS_NAME",
            null,
            "java/lang/Object",
            null
        )

        // 添加静态字段 navList
        val navListField = classWriter.visitField(
            Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC,
            NAV_RUNTIME_NAV_LIST,
            "Ljava/util/ArrayList;",
            "Ljava/util/ArrayList<L${
                NAV_RUNTIME_PKG_NAME.replace(
                    '.',
                    '/'
                )
            }/$NAV_RUNTIME_NAV_DATA_CLASS_NAME;>;",
            null
        )
        navListField.visitEnd()

        // 添加静态初始化块
        val clinit = classWriter.visitMethod(
            Opcodes.ACC_STATIC,
            "<clinit>",
            "()V",
            null,
            null
        )
        clinit.visitCode()

        // 创建 ArrayList
        clinit.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
        clinit.visitInsn(Opcodes.DUP)
        clinit.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/util/ArrayList",
            "<init>",
            "()V",
            false
        )
        clinit.visitFieldInsn(
            Opcodes.PUTSTATIC,
            "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_REGISTRY_CLASS_NAME",
            NAV_RUNTIME_NAV_LIST,
            "Ljava/util/ArrayList;"
        )

        // 添加所有 NavData 到列表
        navDatas.forEach { navData ->
            clinit.visitFieldInsn(
                Opcodes.GETSTATIC,
                "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_REGISTRY_CLASS_NAME",
                NAV_RUNTIME_NAV_LIST,
                "Ljava/util/ArrayList;"
            )

            // 创建新的 NavData 对象
            clinit.visitTypeInsn(
                Opcodes.NEW,
                "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_NAV_DATA_CLASS_NAME"
            )
            clinit.visitInsn(Opcodes.DUP)

            // 调用 NavData 构造函数
            clinit.visitLdcInsn(navData.className)
            clinit.visitLdcInsn(navData.route)
            clinit.visitLdcInsn(navData.type)

            clinit.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_NAV_DATA_CLASS_NAME",
                "<init>",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                false
            )

            // 添加到列表
            clinit.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/util/ArrayList",
                "add",
                "(Ljava/lang/Object;)Z",
                false
            )
            clinit.visitInsn(Opcodes.POP)
        }

        clinit.visitInsn(Opcodes.RETURN)
        clinit.visitMaxs(0, 0)
        clinit.visitEnd()

        // 添加 get 方法
        val getMethod = classWriter.visitMethod(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "get",
            "()Ljava/util/List;",
            "()Ljava/util/List<L${
                NAV_RUNTIME_PKG_NAME.replace(
                    '.',
                    '/'
                )
            }/$NAV_RUNTIME_NAV_DATA_CLASS_NAME;>;",
            null
        )
        getMethod.visitCode()

        getMethod.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
        getMethod.visitInsn(Opcodes.DUP)
        getMethod.visitFieldInsn(
            Opcodes.GETSTATIC,
            "${NAV_RUNTIME_PKG_NAME.replace('.', '/')}/$NAV_RUNTIME_REGISTRY_CLASS_NAME",
            NAV_RUNTIME_NAV_LIST,
            "Ljava/util/ArrayList;"
        )
        getMethod.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/util/ArrayList",
            "<init>",
            "(Ljava/util/Collection;)V",
            false
        )
        getMethod.visitInsn(Opcodes.ARETURN)
        getMethod.visitMaxs(3, 0)
        getMethod.visitEnd()

        classWriter.visitEnd()

        // 修复4: 使用 project.buildDir 来构建输出路径
        val outputDir = File(project.buildDir, "generated/navRegistry")
        outputDir.mkdirs()

        val outputFile = File(outputDir, "$NAV_RUNTIME_REGISTRY_CLASS_NAME.class")
        outputFile.writeBytes(classWriter.toByteArray())

        println("NavPlugin: NavRegistry generated at ${outputFile.absolutePath}")
    }

    private fun handleJarClasses(file: File) {
        // 修复5: 使用 use 函数确保资源被正确关闭
        ZipFile(file).use { zipFile ->
            zipFile.entries().asSequence().forEach { entry ->
                if (entry.name.endsWith(".class")) {
                    zipFile.getInputStream(entry).use { inputStream ->
                        visitClass(inputStream)
                    }
                }
            }
        }
    }

    private fun handleDirectoryClass(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                handleDirectoryClass(it)
            }
        } else if (file.extension.equals("class", true)) {
            // 修复6: 使用 use 函数确保文件流被正确关闭
            FileInputStream(file).use { inputStream ->
                visitClass(inputStream)
            }
        }
    }

    /**
     * 解析 class 文件
     */
    private fun visitClass(inputStream: InputStream) {
        val classReader = ClassReader(inputStream)
        // 修复7: 确保 ClassVisitor 正确处理注解并调用父类方法
        val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                val original = super.visitAnnotation(descriptor, visible)

                if (descriptor != NAV_RUNTIME_DESTINATION) {
                    return original
                }

                // 修复8: 返回自定义的 AnnotationVisitor 而不是简单的对象
                return object : AnnotationVisitor(Opcodes.ASM9, original) {
                    private var route = ""
                    private var type = NavDestination.NavType.None

                    override fun visit(name: String?, value: Any?) {
                        super.visit(name, value)
                        if (name == KEY_ROUTE) {
                            route = value as String
                        }
                    }

                    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
                        super.visitEnum(name, descriptor, value)
                        if (name == KEY_TYPE && descriptor == NAV_RUNTIME_NAV_TYPE) {
                            //type = value ?: NavDestination.NavType.None
                        }
                    }

                    override fun visitEnd() {
                        super.visitEnd()
                        // 获取完成，构建一个 NavData 对象
                        val navData = NavData(
                            classReader.className.replace("/", "."),
                            route,
                            type
                        )
                        navDatas.add(navData)
                        println("NavPlugin: Found NavDestination - $navData")
                    }
                }
            }
        }
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
    }
}

