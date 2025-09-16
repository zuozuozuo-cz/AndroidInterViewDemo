# 通过 VersionCatalogs 管理依赖版本

Libary 定义单独aar，jar依赖，用于所有Moudle 共同使用
bundle 可以将上面的Libary 定义的单独依赖打包成组，简化代码
Version 定义版本号，例如项目版本号，minsdk,targetSdk等等
plugin 定义Gradle 插件版本
toml文件 进行统一管理

# jetPack Navigation
支持Activity Fragment Dialog
支持deekLink 外端唤起
支持 ViewModel，生命周期绑定
SafeArgs 参数安全

## 优缺点
## APIS
### navigate
#### 普通用法，加载启动，清空栈，恢复数据，singleTop
### PopBackStack
#### 清空栈，保存数据，
### navigateUp
#### 默认同PopBackStack 相同，但是如果初始Activity 被销毁后，它会重启Activity，并将当前Fragment作为初始Fragment
### clearBackStack 清空保存过的页面栈，并回到指定页面
### deepLinks 端外唤起
## 缺点
### 需要在xml中维护所有页面，不易维护
### 需要在xml中配置启动节点，不够灵活
### Fragment 使用的是replace 频繁创建与销毁 影响性能
### Fragment SingleTop 模式在栈顶仍然会销毁后再创建 

containerFragment.onInflate(context, attrs, null) attrs即xml 中的属性

@CallSuper
public override fun onInflate(
context: Context,
attrs: AttributeSet,
savedInstanceState: Bundle?
) {
super.onInflate(context, attrs, savedInstanceState)
context.obtainStyledAttributes(attrs, androidx.navigation.R.styleable.NavHost).use { navHost
->
val graphId = navHost.getResourceId(androidx.navigation.R.styleable.NavHost_navGraph, 0)
if (graphId != 0) {
this.graphId = graphId
}
}
context.obtainStyledAttributes(attrs, R.styleable.NavHostFragment).use { array ->
val defaultHost = array.getBoolean(R.styleable.NavHostFragment_defaultNavHost, false)
if (defaultHost) {
defaultNavHost = true
}
}
}

    /**
     * Constructs a new controller for a given [Context]. Controllers should not be used outside of
     * their context and retain a hard reference to the context supplied. If you need a global
     * controller, pass [Context.getApplicationContext].
     *
     * Apps should generally not construct controllers, instead obtain a relevant controller
     * directly from a navigation host via [NavHost.getNavController] or by using one of the utility
     * methods on the [Navigation] class.
     *
     * Note that controllers that are not constructed with an [Activity] context (or a wrapped
     * activity context) will only be able to navigate to
     * [new tasks][android.content.Intent.FLAG_ACTIVITY_NEW_TASK] or
     * [new document tasks][android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT] when navigating to
     * new activities.
     *
     * @param context context for this controller
     */
    init {
        _navigatorProvider.addNavigator(NavGraphNavigator(_navigatorProvider))
        _navigatorProvider.addNavigator(ActivityNavigator(context))
    }

setOnBackPressedDispatcher

protected open fun onCreateNavController(navController: NavController) {
navController.navigatorProvider +=
DialogFragmentNavigator(requireContext(), childFragmentManager)
navController.navigatorProvider.addNavigator(createFragmentNavigator())
} 

val graphId = navHost.getResourceId(androidx.navigation.R.styleable.NavHost_navGraph, 0)
if (graphId != 0) {
this.graphId = graphId
}