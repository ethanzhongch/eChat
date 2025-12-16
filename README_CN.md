# eChat 🤖

![Status](https://img.shields.io/badge/Status-Active%20Development-orange) ![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green)

[English](./README.md) | **中文**

**eChat** 是一个基于 **Kotlin Multiplatform (KMP)** 构建的跨平台 AI 聊天客户端。它作为一个统一的对话接口，允许用户通过 **“自带 Key” (BYOK)** 的方式，与 DeepSeek、Google Gemini、OpenAI 等顶尖大模型进行对话。

> 🚧 **注意：** 本项目目前处于 **活跃开发中** 状态。部分功能仍在实现中，你可能会遇到 Bug。欢迎提交 Issue 或 PR 参与贡献！

## ✨ 核心功能与路线图 (Roadmap)

- [x] **多模型支持：** 轻松配置 DeepSeek、Gemini 等大模型的 API Key。
- [x] **真正的跨平台：** 通过 KMP 技术，原生运行于 Android 和 iOS 设备。
- [x] **隐私优先：** 你的 API Key 和聊天记录仅保存在本地设备上，绝不上传第三方服务器。
- [ ] **聊天记录：** 本地持久化存储对话历史。(即将推出)
- [ ] **Markdown 支持：** 优化代码块、表格和数学公式的渲染。(即将推出)
- [ ] **流式响应：** 支持打字机效果的实时回复。

---

## 🛠 技术栈与架构

本项目完全使用 **Kotlin Multiplatform (KMP)** 配合 **Compose Multiplatform (CMP)** 构建。

**为什么选择 KMP？**
* **原生性能：** 业务逻辑直接编译为原生二进制文件（Android 编译为 JVM 字节码，iOS 通过 LLVM 编译为机器码），没有运行时损耗。
* **统一开发体验：** 我们共享了 **100% 的业务逻辑**（API 客户端、ViewModel、数据库）以及 **95% 以上的 UI** 代码。
* **无缝互操作：** 在需要时，可以随时调用 iOS (SwiftUI/UIKit) 或 Android 的原生 API。

### ⚖️ 跨平台方案对比

我们在 2025 年选择 KMP 而非 Flutter 或 React Native 的原因：

| 特性 | **Kotlin Multiplatform (CMP)** | **Flutter** | **React Native** |
| :--- | :--- | :--- | :--- |
| **性能** | **原生级** (iOS 端无虚拟机/无桥接) | 高 (Dart VM + 自绘引擎) | 不错 (JS Bridge 存在通信损耗) |
| **UI 渲染** | **Skia (Canvas)** / 可回退原生 | Skia / Impeller (完全自绘) | JS 驱动原生组件 |
| **逻辑共享** | **100% 共享** (网络, 数据库, 算法) | 共享 (Dart 语言) | 共享 (JS/TS 语言) |
| **生态系统** | 直接复用 Android/Kotlin 庞大生态 | 独立的 Dart 生态 | NPM / JavaScript 生态 |

---

## 📂 项目结构

* **[`/composeApp`](./composeApp/src)**: 核心共享模块。
    * `commonMain`: 所有平台通用的代码（UI 界面, ViewModel, Ktor 网络请求逻辑）。
    * `androidMain`: Android 特有的实现代码。
    * `iosMain`: iOS 特有的实现代码（例如调用 Apple Crypto 库）。

* **[`/iosApp`](./iosApp/iosApp)**: iOS 原生入口。
    * 包含 Xcode 工程 (`.xcodeproj`) 和用于承载 Compose 页面的 SwiftUI 包装器。

## 🚀 快速开始

### Android

构建并运行 Android 应用：
* 在 Android Studio 的运行配置中选择 `composeApp`。
* 或者直接在终端运行：
    ```shell
    ./gradlew :composeApp:assembleDebug
    ```
  *(Windows 用户请使用 `gradlew.bat`)*

### iOS

构建并运行 iOS 应用：
* 在 Xcode 中打开 **[`/iosApp`](./iosApp)** 目录并点击运行。
* 或者在 Android Studio 中使用 **Kotlin Multiplatform Mobile** 插件运行。

---
*Built with ❤️ using [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/).*