# 哈基米 (Hajimi) - 治愈系本地音乐播放器

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green.svg)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Material3-Design-purple.svg)](https://m3.material.io/)
[![Media3](https://img.shields.io/badge/Media3-ExoPlayer-orange.svg)](https://developer.android.com/jetpack/androidx/releases/media3)

> **“哈基米一下，烦恼全放下”** 😺🎵

**哈基米 (Hajimi)** 是一款基于 Android 平台的治愈系本地音乐播放器，采用 Jetpack Compose 构建。它不仅拥有媲美 iOS 原生应用的高保真 UI 设计，更融入了可爱的猫咪元素，为您带来轻松、愉悦的音乐体验。

## ✨ 核心亮点

### 📸 App 截图展示

| 音乐库 (Library) | 播放器 (Player) | 猫窝 (Pet) | 聊天 (Chat) |
|:---:|:---:|:---:|:---:|
| <img width="280"  alt="library" src="https://github.com/user-attachments/assets/e518931d-0334-4fd2-a9bb-cafe27a0aa20" />| <img width="280"  alt="player" src="https://github.com/user-attachments/assets/ef221f75-a77b-40aa-9c9b-39ee97874d0d" />| <img width="280" alt="pet" src="https://github.com/user-attachments/assets/be195872-e3ca-4b28-ac26-46a9148923a4" />| <img width="280" alt="chat" src="https://github.com/user-attachments/assets/placeholder_chat.png" />|
| *多标签页管理 / 智能搜索* | *沉浸式背景 / 拖动进度条* | *治愈系猫咪互动* | *AI 猫娘对话 / 微信风 UI* |

*   **🐱 治愈系品牌形象**
    *   **萌系设计**：全新的“音符+猫爪”图标与粉蓝配色主题。
    *   **猫咪元素植入**：随处可见的猫咪彩蛋，无封面歌曲自动显示超萌猫咪写真。
    *   **趣味互动**：点击“喜欢”按钮时触发“哈基米～”弹幕/Toast 彩蛋。
    *   **AI 聊天伴侣**：内置 DeepSeek 驱动的“傲娇猫娘”AI，支持流式对话，根据 MBTI/星座个性化回复，采用亲切的微信聊天风格，和你心里的那个她聊聊天吧。
    *   **自定义封面**：支持根据歌手名自动匹配本地预置的歌手封面（如周杰伦、陈奕迅等），如果找不到则显示默认的“戴耳机听歌猫咪”Logo。

*   **🎧 沉浸式播放体验**
    *   **高斯模糊背景**：全屏毛玻璃效果，背景色随专辑封面动态变化。
    *   **Edge-to-Edge**：完全适配全面屏，无顶部标题栏遮挡，视觉通透。
    *   **手势操作**：支持左右滑动切歌，丝滑的转场动画。
    *   **交互式进度条**：支持**拖动进度条**快进/快退，实时预览时间。
    *   **多种循环模式**：支持**无循环**、**单曲循环**、**列表循环**三种模式切换。
    *   **全局 MiniPlayer**：底部的悬浮播放条，随时随地控制音乐，支持歌手封面显示（聊天界面自动隐藏）。
    *   **原生媒体通知**：集成 Android 系统级媒体通知栏（MediaStyle），支持锁屏控制和通知栏切歌。

*   **🎵 智能音乐库**
    *   **多标签页管理**：支持“全部歌曲”、“收藏”、“最近播放”、“下载”四个页面，**左右滑动**丝滑切换。
    *   **智能扫描**：自动识别本地存储中的音频文件，支持读取 FLAC 等无损格式，并自动解析**内嵌封面**。
    *   **便捷管理**：
        *   **长按删除**：长按歌曲条目触发“蓄力删除”特效（红色进度条动画）。
        *   **一键清空**：右上角菜单提供“清空音乐库”功能，支持二次确认。
        *   **重新扫描**：右上角菜单支持手动触发全盘扫描。
    *   **搜索功能**：顶部常驻搜索栏，快速定位心仪歌曲。

*   **📂 播放列表系统**
    *   创建与管理自定义播放列表。
    *   iOS 风格的分组设置界面（深色模式、音效调节、高音质优先等开关演示）。
    *   支持顶部 Segmented Control（分段控件）快速切换“我的列表”与“相关设置”。

## 🛠️ 技术栈

*   **语言**：Kotlin
*   **UI 框架**：Jetpack Compose (Material3)
*   **架构模式**：MVVM (Model-View-ViewModel)
*   **媒体播放**：AndroidX Media3 (ExoPlayer)
*   **图片加载**：Coil (Coroutine Image Loading)
*   **导航**：Navigation Compose
*   **构建工具**：Gradle (Kotlin DSL)

## 📱 原型设计

本项目包含一套完整的高保真 HTML 原型，位于 `docs/music-player` 目录下。
原型采用 **Tailwind CSS** + **FontAwesome** 构建，模拟了 **iPhone 15 Pro** 的外壳展示效果。
<img width="1982" height="1217" alt="image" src="https://github.com/user-attachments/assets/78faa3f8-3e58-457f-9c44-c311bff69939" />

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
    git clone https://github.com/zzzjian1/MusicApp.git
    ```
2.  使用 Android Studio 打开项目根目录 `d:\MusicApp`。
3.  等待 Gradle Sync 完成。
4.  连接 Android 真机或启动模拟器。
5.  运行命令或点击 IDE 运行按钮：
    ```bash
    ./gradlew assembleDebug
    ```
6.  **注意**：首次运行时，App 需要获取**存储权限**（读取音频文件）和**网络权限**（加载猫咪美图），请在弹窗中允许。

### 歌手封面配置
如果您希望显示特定歌手的照片，请按以下步骤操作：
1.  将歌手图片（如 `jay_chou.jpg`）放入 `app/src/main/res/drawable/` 目录。
2.  修改 `app/src/main/java/com/zzzjian/music/utils/ArtistImageMapper.kt` 文件。
3.  在 `artistMap` 中添加映射，例如：`"周杰伦" to R.drawable.jay_chou`。

## 📂 项目结构

```
MusicApp/
├── app/
│   ├── src/main/java/com/zzzjian/music/
│   │   ├── data/media/         # 媒体数据层 (MediaScanner)
│   │   ├── domain/model/       # 数据模型 (Song, Playlist, PlaybackState, MockData)
│   │   ├── player/             # 播放器核心逻辑 (PlayerManager)
│   │   ├── ui/
│   │   │   ├── screens/        # Compose 页面 (Library, Player, Playlists)
│   │   │   ├── theme/          # 主题与配色 (Color, Type, Theme)
│   │   │   └── components/     # 通用组件
│   │   ├── utils/              # 工具类 (ArtistImageMapper)
│   │   ├── MainActivity.kt     # 主入口与导航图
│   │   └── PlayerViewModel.kt  # 视图模型
│   └── src/main/AndroidManifest.xml
├── docs/
│   └── music-player/           # HTML 高保真原型
└── README.md                   # 项目文档
```

## 📝 待办事项 (TODO)

- [x] App 品牌升级为“哈基米”
- [x] 适配沉浸式状态栏 (Edge-to-Edge)
- [x] 音乐库多标签页滑动切换
- [x] 集成猫咪主题 UI 与彩蛋
- [x] 实现播放进度条拖动
- [x] 优化本地封面扫描与显示逻辑
- [x] 支持歌手自定义封面配置
- [x] 增加“一键清空音乐库”功能
- [x] 实现原生 MediaStyle 通知栏控制
- [x] 集成 DeepSeek AI 聊天室
- [ ] 实现真实的播放列表增删改查（目前为 UI 演示）。
- [ ] 完善“收藏”与“最近播放”功能的持久化存储（Room Database）。
- [ ] 适配深色模式（Dark Mode）。

## 📄 许可证

MIT License
