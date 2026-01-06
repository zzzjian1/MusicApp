# MusicApp - Android 高保真本地音乐播放器

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green.svg)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Material3-Design-purple.svg)](https://m3.material.io/)
[![Media3](https://img.shields.io/badge/Media3-ExoPlayer-orange.svg)](https://developer.android.com/jetpack/androidx/releases/media3)

MusicApp 是一款基于 Android 平台的现代化本地音乐播放器，采用 Jetpack Compose 构建，拥有媲美 iOS 原生应用的高保真 UI 设计。它专注于提供简洁、流畅且美观的音乐播放体验。

## ✨ 功能特性

*   **🎧 沉浸式播放体验**
    *   全屏毛玻璃高斯模糊背景，随专辑封面动态变化。
    *   大尺寸唱片封面展示。
    *   支持**无循环**、**单曲循环**、**列表循环**三种播放模式切换。
    *   实时进度控制与播放状态同步。
    *   全局 MiniPlayer 悬浮条，随时随地控制音乐。

*   **🎵 音乐库管理**
    *   自动扫描本地存储中的音频文件。
    *   支持按**歌曲**、**艺术家**、**专辑**等维度浏览。
    *   便捷的顶部搜索栏与分类标签（全部歌曲、收藏、最近播放、下载）。

*   **📂 播放列表系统**
    *   创建与管理自定义播放列表。
    *   iOS 风格的分组设置界面（深色模式、音效调节、高音质优先等开关演示）。
    *   支持顶部 Segmented Control（分段控件）快速切换“我的列表”与“相关设置”。

*   **🎨 高保真 UI/UX**
    *   **仿 iOS 视觉风格**：圆角卡片、细腻阴影、扁平化图标。
    *   **统一底部导航**：扁平化白色底栏，集成 Album/List/Home 图标，视觉清爽。
    *   **交互细节**：按钮点击反馈、平滑的页面过渡动画。

## 🛠️ 技术栈

*   **语言**：Kotlin
*   **UI 框架**：Jetpack Compose (Material3)
*   **架构模式**：MVVM (Model-View-ViewModel)
*   **媒体播放**：AndroidX Media3 (ExoPlayer)
*   **图片加载**：Coil (Coroutine Image Loading)
*   **导航**：Navigation Compose
*   **依赖注入**：Hilt (虽然当前规模较小未使用，但架构已预留扩展性)
*   **构建工具**：Gradle (Kotlin DSL)

## 📱 原型设计
![Uploading image.png…]()

本项目包含一套完整的高保真 HTML 原型，位于 `docs/music-player` 目录下。
原型采用 **Tailwind CSS** + **FontAwesome** 构建，模拟了 **iPhone 15 Pro** 的外壳展示效果。

*   **在线预览**：可直接在浏览器打开 `docs/music-player/index.html` 查看。
*   **设计对照**：Android 端代码严格还原了 HTML 原型的布局与视觉细节。

## 🚀 快速开始

### 环境要求
*   Android Studio Iguana | 2023.2.1 或更高版本
*   JDK 17
*   Android SDK API 34 (UpsideDownCake)

### 编译运行
1.  克隆项目到本地：
    ```bash
    git clone <repository-url>
    ```
2.  使用 Android Studio 打开项目根目录 `d:\MusicApp`。
3.  等待 Gradle Sync 完成。
4.  连接 Android 真机或启动模拟器。
5.  运行命令或点击 IDE 运行按钮：
    ```bash
    ./gradlew assembleDebug
    ```
6.  **注意**：首次运行时，App 需要获取**存储权限**（读取音频文件），请在弹窗中允许。

## 📂 项目结构

```
MusicApp/
├── app/
│   ├── src/main/java/com/zzzjian/music/
│   │   ├── domain/model/       # 数据模型 (Song, Playlist, PlaybackState)
│   │   ├── player/             # 播放器核心逻辑 (PlayerManager)
│   │   ├── ui/
│   │   │   ├── screens/        # Compose 页面 (Library, Player, Playlists)
│   │   │   ├── theme/          # 主题与配色 (Color, Type, Theme)
│   │   │   └── components/     # 通用组件
│   │   ├── MainActivity.kt     # 主入口与导航图
│   │   └── PlayerViewModel.kt  # 视图模型
│   └── src/main/AndroidManifest.xml
├── docs/
│   └── music-player/           # HTML 高保真原型
└── README.md                   # 项目文档
```

## 📝 待办事项 (TODO)

- [ ] 实现真实的播放列表增删改查（目前为 UI 演示）。
- [ ] 完善“收藏”与“最近播放”功能的持久化存储（Room Database）。
- [ ] 适配深色模式（Dark Mode）。
- [ ] 添加歌词滚动显示功能。

## 📄 许可证

MIT License
