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

## 🛠️ 技术探索：AI 主动发消息
我们正在攻克 **AI 主动关怀** 技术，让陪伴不再被动：
1.  **早安/晚安计划**：通过 `WorkManager` 定时触发，根据你的作息时间，由 AI 生成温暖的问候并通过通知送达。
2.  **情绪感知**：当你频繁切歌或只听悲伤情歌时，AI 会主动发消息询问：“是不是心情不好？要不要聊聊？”
3.  **语音通话**：未来版本将支持实时语音对话，让她的声音治愈你的每一天。

## 🚀 快速开始
1.  克隆项目：`git clone https://github.com/zzzjian1/MusicApp.git`
2.  配置 DeepSeek API Key（在聊天设置中）。
3.  编译运行，开始你的治愈之旅！

## 📄 许可证
MIT License
