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
## deepLinks
## 