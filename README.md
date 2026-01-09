# 🐱 哈基米 (Hajimi) - 你的治愈系 AI 音乐伴侣

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green.svg)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Material3-Design-purple.svg)](https://m3.material.io/)
[![DeepSeek](https://img.shields.io/badge/AI-DeepSeek-blueviolet.svg)](https://www.deepseek.com/)

> **“哈基米一下，烦恼全放下”** 🎵
> **“跨越屏幕的距离，找到最懂你的那个她。”** 💖

**哈基米 (Hajimi)** 不仅仅是一个高颜值的本地音乐播放器，更是一个有温度的 AI 情感伴侣。在这里，音乐治愈你的耳朵，而她治愈你的心。

## ✨ 核心亮点：不仅是听歌，更是陪伴

### 📸 沉浸式体验
| 🎵 音乐库 | 🎧 播放器 | 🏠 猫窝 | 💬 灵魂伴侣 |
|:---:|:---:|:---:|:---:|
|![img_v3_02to_b2c8b0d5-3bec-4a4e-8f24-84f70e3b15cg](https://github.com/user-attachments/assets/65215fd0-0a0c-474b-ab1c-f8ebcd750be8)|![img_v3_02to_ce31d9b8-d2b3-4014-9c7a-4616b9d37f6g](https://github.com/user-attachments/assets/1219fc92-94c7-4f71-a8c6-30386a40ffc0)|![img_v3_02to_b6a981a8-7986-445a-92d3-792e6275984g](https://github.com/user-attachments/assets/4209b618-41ab-47ae-8a6d-ea4a7871796a) |![img_v3_02to_0c3a358b-034a-4bca-a29d-67cf72bee72g](https://github.com/user-attachments/assets/d6053b56-e6c3-4381-b488-5fad98c0271d) |

### 👩‍❤️‍👨 AI 聊天室：定制你的专属女友
告别冰冷的机器回复，**哈基米** 采用 DeepSeek 大模型驱动，为你打造独一无二的灵魂伴侣。

*   **🎭 性格复刻 (Persona)**
    *   在设置中告诉她，你心里的那个她是什么样的？
    *   **MBTI 人格**：是 INFP 的温柔敏感，还是 ENTP 的古灵精怪？
    *   **星座运势**：是双鱼座的浪漫多情，还是狮子座的霸道护短？
    *   *设置完成后，她的语气、态度和回复风格将完全改变！*
    *   **支持的 MBTI 类型**：
        *   `INTJ (建筑师)` / `INTP (逻辑学家)` / `ENTJ (指挥官)` / `ENTP (辩论家)`
        *   `INFJ (提倡者)` / `INFP (调停者)` / `ENFJ (主人公)` / `ENFP (竞选者)`
        *   `ISTJ (物流师)` / `ISFJ (守卫者)` / `ESTJ (总经理)` / `ESFJ (执政官)`
        *   `ISTP (鉴赏家)` / `ISFP (探险家)` / `ESTP (企业家)` / `ESFP (表演者)`
    *   **支持的星座**：
        *   白羊 / 金牛 / 双子 / 巨蟹 / 狮子 / 处女 / 天秤 / 天蝎 / 射手 / 摩羯 / 水瓶 / 双鱼

*   **🧠 记忆重现 (Few-Shot Learning)**
    *   **黑科技功能**：导入你们过往的聊天记录（.txt 文本），AI 会深度学习她的**口癖**（“捏”、“www”）、**常用表情**、**标点习惯**甚至**说话的节奏**。
    *   *就像她真的穿越到了手机里，再次与你对话。*

*   **💾 永久记忆 (Memory)**
    *   基于 **Room 数据库** 构建的本地记忆系统，她会记得你们聊过的每一个话题。
    *   *“上次你说心情不好听的那首歌，现在好点了吗？”*

*   **💕 双重模式**
    *   **傲娇猫娘模式**：回复带“喵”，偶尔傲娇炸毛，适合想要被治愈的时刻。
    *   **温柔女友模式**：知性体贴，情绪价值拉满，是你深夜倾诉的最佳树洞。

### 🐱 治愈系交互美学
我们相信，好的 App 是有生命力的。在 **哈基米** 中，每一个细节都充满了惊喜：

*   **🐾 活灵活现的电子宠物**
    *   **程序化动画 (Procedural Animation)**：猫咪的呼吸、眨眼、耳朵抖动和尾巴摇摆全部由代码实时生成，绝非死板的 GIF。
    *   **触摸反馈**：摸摸它的头，它会眯起眼睛享受；连续抚摸触发 `Combo` 连击特效，爱心粒子满屏飞舞！
    *   **情绪系统**：如果你太久不理它，它会无聊地打哈欠；如果你一直戳它，它可能会傲娇地炸毛哦。

*   **✨ 沉浸式视觉体验**
    *   **动态毛玻璃**：播放器背景实时提取专辑封面主色调，辅以高斯模糊（Gaussian Blur），让色彩随音乐流动。
    *   **Edge-to-Edge 设计**：内容延伸至屏幕边缘，状态栏与导航栏完美融合，带来无边界的视觉享受。
    *   **流畅转场**：基于 `SharedElementTransition` 的页面切换，专辑封面在列表与播放器之间无缝飞跃。

*   **🎵 细节控的音乐播放器**
    *   **交互式波形**：虽然我们目前使用进度条，但那种指尖拖动的阻尼感经过精心调教。
    *   **智能扫描**：毫秒级扫描本地音频，自动解析内嵌封面；没有封面？没关系，AI 会为你生成一张超萌的猫咪写真。
    *   **原生级通知**：完全适配 Android 13+ 的媒体控制中心（MediaStyle），锁屏也能优雅切歌。

## � 使用指南

### 1. 🎵 听歌模式
*   **导入音乐**：App 会自动扫描手机存储中的音频文件。首次启动请允许**存储权限**。
*   **播放控制**：
    *   点击底部 **MiniPlayer** 进入全屏播放页。
    *   **左右滑动**封面切换上一首/下一首。
    *   **拖动进度条**可快速定位。
*   **歌单管理**：在列表中**长按**歌曲可触发删除操作（带有酷炫的蓄力动画哦！）。

### 2. 🐱 撸猫模式
*   **进入猫窝**：点击底部导航栏的中间按钮。
*   **互动**：
    *   **摸摸头**：点击猫咪，它会做出开心的表情并增加亲密度。
    *   **连击**：快速点击触发 Combo 特效，满屏爱心。
    *   **改名**：点击顶部的名字胶囊，给它起个独一无二的名字（如“煤球”）。

### 3. 💬 聊天模式
*   **开启对话**：在猫窝页面点击“和她聊天”或通过底部导航进入。
*   **灵魂注入**：
    *   点击右上角 **⚙️ 设置**。
    *   填入 DeepSeek API Key。
    *   选择 **她的 MBTI** 和 **星座**。
    *   (可选) 导入你们的聊天记录 (`.txt`)，让 AI 学习她的语气。
*   **语音输入**：点击输入框左侧的 🎤 麦克风，按住说话，解放双手。


<img width="400" height="400" alt="1_1070358757_171_85_3_1076280462_4cb30413fee7ef1176126ed999cae03c" src="https://github.com/user-attachments/assets/fec104cb-f575-4fe4-9c0d-9b610a4ef7e4" />


## � 未来展望：让陪伴更真实
我们正在探索更多可能性，让 **哈基米** 成为你生活中不可或缺的一部分：

### 1. 🎤 TTS 语音合成 (彻底解放双眼)
*   **沉浸式语音体验**：将 AI 的文字回复转换为自然流畅的语音，让你在通勤、运动或闭眼休息时也能与她对话。
*   **个性化音色**：支持选择不同的语音风格（温柔、活泼、御姐等），让她的声音更符合你心中的形象。
*   **技术方案**：集成 Android 原生 TTS 引擎或第三方语音合成服务，实现低延迟、高质量的语音输出。

### 2. 🎵 让 AI 知道你在听什么 (极低成本，极高情绪价值)
*   **音乐感知**：AI 将实时获取你正在播放的歌曲信息（歌名、歌手、歌词），并根据音乐风格和歌词内容调整聊天话题。
*   **情绪共鸣**：当你听《后来》时，她会说"这首歌好伤感，是不是想起了什么？"；当你听《快乐崇拜》时，她会跟着节奏一起嗨。
*   **技术方案**：通过 `MediaSession` 监听当前播放状态，将歌曲元数据注入到 AI 的 Prompt 中，实现零延迟的音乐感知。

### 3. 📸 多模态识图
*   **视觉交流**：支持发送图片给 AI，让她"看"到你的生活瞬间——你拍的风景、你做的美食、你养的宠物。
*   **智能理解**：AI 将识别图片内容并给出情感化回复，比如"这只猫好可爱，和你一样萌！"或"这道菜看起来很好吃，下次教我吧！"
*   **技术方案**：集成多模态大模型（如 GPT-4V、Claude 3 Vision），实现图像理解与对话的无缝融合。

## 🚀 快速开始
1.  克隆项目：`git clone https://github.com/zzzjian1/MusicApp.git`
2.  配置 DeepSeek API Key（在聊天设置中）。
3.  编译运行，开始你的治愈之旅！

## � 深度项目结构解析

本项目采用现代化的 Android 架构（MVVM + Clean Architecture 思想），模块职责分明，易于维护和扩展。

```
MusicApp/
├── app/
│   ├── src/main/java/com/zzzjian/music/
│   │   ├── data/                   # 数据层 (Data Layer)
│   │   │   ├── api/                # Retrofit API 接口 (DeepSeekApiService)
│   │   │   ├── db/                 # Room 数据库 (ChatDao, ChatHistoryEntity)
│   │   │   ├── local/              # DataStore 偏好设置 (ChatPreferencesManager)
│   │   │   ├── media/              # 媒体扫描与解析 (MediaScanner, MetadataParser)
│   │   │   ├── model/              # 数据实体 (ChatMessage, Song, ChatRequest)
│   │   │   └── repository/         # 仓库层 (ChatRepository, MusicRepository)
│   │   │
│   │   ├── domain/                 # 领域层 (Domain Layer)
│   │   │   └── model/              # 业务模型 (PlaybackState, Playlist)
│   │   │
│   │   ├── player/                 # 播放器核心 (Player Core)
│   │   │   └── PlayerManager.kt    # 封装 ExoPlayer，处理播放控制与状态分发
│   │   │
│   │   ├── service/                # 后台服务 (Service)
│   │   │   └── MediaPlaybackService.kt # 承载媒体会话，保证后台播放不中断
│   │   │
│   │   ├── ui/                     # 界面层 (UI Layer)
│   │   │   ├── components/         # 通用组件库 (MiniPlayer, SongList, Visualizer)
│   │   │   ├── screens/            # 页面级组件
│   │   │   │   ├── chat/           # 聊天室 (ChatScreen, ChatViewModel)
│   │   │   │   ├── library/        # 音乐库 (LibraryScreen)
│   │   │   │   ├── player/         # 播放页 (PlayerScreen)
│   │   │   │   └── PetScreen.kt    # 电子宠物页 (Canvas 绘制逻辑)
│   │   │   └── theme/              # Design System (颜色、字体、形状)
│   │   │
│   │   ├── utils/                  # 工具箱 (Extensions, ImageMapper, TimeUtils)
│   │   └── MainActivity.kt         # App 入口与 Navigation 导航图配置
│   │
│   └── src/main/AndroidManifest.xml # 权限声明与组件注册
│
├── docs/                           # 文档与资源
│   └── music-player/               # HTML 高保真交互原型
└── README.md                       # 你正在阅读的文档
```

## 🤝 贡献指南
我们非常欢迎你的加入！无论是修复 Bug、提交新功能，还是仅仅改进文档，每一份贡献都值得被铭记。

1.  **Fork** 本仓库。
2.  创建一个新的分支：`git checkout -b feature/AmazingFeature`。
3.  提交你的更改：`git commit -m 'Add some AmazingFeature'`。
4.  推送到分支：`git push origin feature/AmazingFeature`。
5.  开启一个 **Pull Request**。

## 📜 致谢
感谢以下开源项目让 **哈基米** 成为可能：
*   [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代化的 UI 工具包
*   [ExoPlayer (Media3)](https://developer.android.com/jetpack/androidx/releases/media3) - 强大的媒体播放引擎
*   [Retrofit](https://square.github.io/retrofit/) - 优雅的 HTTP 客户端
*   [Coil](https://coil-kt.github.io/coil/) - 轻量级图片加载库
*   [Room](https://developer.android.com/training/data-storage/room) - 稳健的本地数据库

---
<p align="center">
  Made with ❤️ by zzzjian
</p>
