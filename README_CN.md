# eChat 🤖

![Status](https://img.shields.io/badge/Status-%E5%BC%80%E5%8F%91%E4%B8%AD-orange) ![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20Desktop-green) ![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue) ![Compose](https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-purple)

[English](./README.md) | **中文**

**eChat** 是一个基于 **Kotlin Multiplatform (KMP)** 构建的跨平台 AI 聊天客户端。它作为一个统一的接口，支持多种 LLM（大语言模型）供应商，允许用户通过 **自带密钥 (BYOK)** 的方式与 DeepSeek、Gemini 和 OpenAI 等顶级模型进行对话。

---

### ⚡️ Vibe Coding 展示 (AI 编程)

> **本项目是一个 100% 由 AI 辅助创作的作品。**
>
> * **代码与逻辑:** 使用 **Cursor**, **Antigravity**, **Codex**, 和 **Gemini CLI** 构建。
> * **UI 与交互:** 使用 [**Stitch**](https://stitch.withgoogle.com) 和 **Figma AI** 进行原型设计。
>
> 🧠 **对开发过程感兴趣？**
> * [**查看开发提示词**](./docs/vibe_coding/prompt.md): 查看用于构建 App 逻辑的完整提示词记录。
> * [**查看 UI/UX 生成提示词**](./docs/ui_ux/prompt.md): 查看我们是如何生成 UI 资产的。

![UI UX Overview](./docs/ui_ux/ui_ux.png)

---

## ✨ 核心功能与路线图

- [x] **多模型支持:** 配置 DeepSeek, Gemini, 和 OpenAI 的 API 密钥。
- [x] **跨平台:** 通过 KMP 在 Android 和 iOS 上原生运行。
- [x] **隐私优先:** API 密钥和聊天记录完全本地存储 (DataStore & Room)。
- [x] **聊天记录:** 离线持久化存储对话历史。
- [x] **Markdown 支持:** 富文本渲染和代码块高亮。
- [x] **智能 UX:** 错误拦截和“空状态”引导。
- [ ] **流式响应 (Streaming):** 实时打字机效果。
- [ ] **桌面端支持:** 原生 PC 版本 (Windows/macOS/Linux)。
- [ ] **语音功能:** AI 回复的 TTS (文本转语音) 支持。

---

## 🚀 快速开始

### Android

在 Android 上构建并运行应用:
* 在 Android Studio 中选择 `composeApp` 配置。
* 或者通过终端运行:
    ```shell
    ./gradlew :composeApp:assembleDebug
    ```
  *(Windows 用户: 请使用 `gradlew.bat`)*

### iOS

在 iOS 上构建并运行应用:
* 在 Xcode 中打开 **[`/iosApp`](./iosApp)** 目录。
* 或者在 Android Studio 中使用 **Kotlin Multiplatform Mobile** 插件配置运行。

---

## 🛠 技术栈与架构

本项目使用 **Kotlin Multiplatform (KMP)** 和 **Compose Multiplatform (CMP)** 构建。

**为什么选择 KMP?**
* **原生性能:** 逻辑直接编译为原生二进制文件（Android 为 JVM，iOS 为 LLVM，桌面端为 Native），确保零运行时开销。
* **统一开发:** 共享 **100% 的业务逻辑** (API 客户端, ViewModel, 数据库) 和 **95%+ 的 UI** 代码。
* **无缝互操作:** 需要时可完全访问平台特定 API。

### ⚖️ 跨平台方案对比

为什么我们为 eChat 选择 KMP 而不是 Flutter 或 React Native:

| 特性 | **Kotlin Multiplatform (CMP)** | **Flutter** | **React Native** |
| :--- | :--- | :--- | :--- |
| **性能** | **原生** (iOS 无桥接/虚拟机) | 高 (Dart VM + 自定义引擎) | 良好 (JS 桥接开销) |
| **UI 渲染** | **Skia (Canvas)** / 原生回退 | Skia / Impeller (自定义) | 通过 JS 调用原生组件 |
| **逻辑共享** | **100% 共享** (网络, SQL) | 共享 (Dart) | 共享 (JS/TS) |
| **生态系统** | 复用 Android/Kotlin 库 | Dart 特定生态 | NPM / JavaScript 生态 |

### 🏗️ 为什么选择 MVI 架构?

本项目采用了 **Model-View-Intent (MVI)** 模式结合单向数据流 (UDF)。

* **单一数据源 (Single Source of Truth):** UI 观察单一的 `UiState` 对象，消除了复杂聊天应用中常见的状态冲突 Bug。
* **可预测性 (Predictability):** 状态变更只能通过 ViewModel 处理特定的 `Intents` 来触发，使逻辑易于追踪和调试。
* **线程安全 (Thread Safety):** 状态突变在 ViewModel 内部串行化，避免了 KMP 多线程环境下的竞争条件。

#### 1. 系统组件 (结构视图)

```mermaid
graph TD
    %% --- 样式定义 ---
    classDef platform fill:#E3F2FD,stroke:#1565C0,stroke-width:2px,rx:10,ry:10;
    classDef ui fill:#FFF3E0,stroke:#EF6C00,stroke-width:2px,rx:10,ry:10;
    classDef presentation fill:#E8F5E9,stroke:#2E7D32,stroke-width:2px,rx:5,ry:5;
    classDef domain fill:#F3E5F5,stroke:#7B1FA2,stroke-width:2px,rx:5,ry:5;
    classDef data fill:#E0F7FA,stroke:#006064,stroke-width:2px,rx:5,ry:5;
    classDef remote fill:#FFEBEE,stroke:#C62828,stroke-width:2px,stroke-dasharray: 5 5;

    %% --- 1. 平台层 ---
    subgraph Platforms ["📱 平台层 (Platform Layer)"]
        direction TB
        Android["🤖 Android Activity<br/>(MainActivity)"]:::platform
        iOS["🍎 iOS App<br/>(SwiftUI App / MainViewController)"]:::platform
    end

    %% --- 2. 共享模块 ---
    subgraph CommonMain ["📦 共享模块 (commonMain)"]
        direction TB

        %% --- A. UI 层 ---
        subgraph UI_Layer ["🎨 CMP UI 层"]
            AppEntry["App Composable 入口"]:::ui
            ChatScreen["聊天页面 (ChatScreen)"]:::ui
            UI_Components["Compose 组件<br/>(气泡, 输入栏)"]:::ui
        end
        
        %% --- B. 表现层 ---
        subgraph Presentation ["🧠 表现层 (MVI)"]
            ViewModel["ChatViewModel"]:::presentation
            State["ChatUiState<br/>(不可变状态)"]:::presentation
            Intent["ChatIntent<br/>(密封接口)"]:::presentation
        end

        %% --- C. 数据/领域层 ---
        subgraph Data_Layer ["🏗️ 数据层"]
            Repository["ChatRepository"]:::domain
            Model["领域模型<br/>(ChatMessage, Session)"]:::domain
        end

        %% --- D. 数据源 ---
        subgraph Data_Sources ["💾 数据源"]
            subgraph Local ["本地存储"]
                RoomDB["🗄️ Room 数据库<br/>(SQLite Bundled)"]:::data
                DataStore["⚙️ DataStore<br/>(设置 / API Key)"]:::data
            end
            
            subgraph Remote_Source ["网络"]
                Ktor["🌐 Ktor 客户端"]:::data
                LlmStrategy["LLM 策略 / 工厂模式"]:::data
            end
        end
    end

    %% --- 3. 外部服务 ---
    subgraph Cloud ["☁️ 外部 LLM 供应商"]
        DeepSeek["DeepSeek API"]:::remote
        Gemini["Google Gemini API"]:::remote
        OpenAI["OpenAI API"]:::remote
    end

    %% --- 连接关系 ---
    
    %% Platform -> UI
    Android -->|SetContent| AppEntry
    iOS -->|ComposeUIViewController| AppEntry
    AppEntry --> ChatScreen
    ChatScreen --> UI_Components

    %% UI -> ViewModel (MVI Loop)
    ChatScreen -- "1. 发送 Intent" --> Intent
    Intent --> ViewModel
    ViewModel -- "4. 发射新 State" --> State
    State -- "5. 渲染 UI" --> ChatScreen

    %% ViewModel -> Repository
    ViewModel -- "2. sendMessage()" --> Repository
    Repository -- "3. Flow&lt;Data&gt;" --> ViewModel
    Repository -.-> Model

    %% Repository -> Sources
    Repository -->|读/写| RoomDB
    Repository -->|获取 API Key| DataStore
    Repository -->|选择策略| LlmStrategy
    LlmStrategy --> Ktor

    %% Network Flow
    Ktor --> DeepSeek
    Ktor --> Gemini
    Ktor --> OpenAI
    
    %% DI Injection
    Koin(("💉 Koin 依赖注入")) -.->|注入| ViewModel
    Koin -.->|注入| Repository
    Koin -.->|注入| RoomDB
    Koin -.->|注入| Ktor

    %% Link Styles
    linkStyle default stroke:#546E7A,stroke-width:2px;
```

#### 2. 消息数据流 (时序图)

此时序图展示了一条聊天消息的完整生命周期：从用户输入到 AI 流式响应，重点展示了乐观 UI 更新 (Optimistic UI updates) 和数据持久化过程。

```mermaid
sequenceDiagram
    autonumber
    actor User as 👤 用户
    participant UI as 📱 Compose UI
    participant VM as 🧠 ChatViewModel
    participant Repo as 📦 ChatRepository
    participant DB as 🗄️ Room DB
    participant Net as 🌐 Ktor Client

    %% 1. 用户操作
    User->>UI: 输入文本并点击 "发送"
    
    %% 2. MVI Intent
    UI->>VM: 分发 Intent: SendMessage(text)
    
    %% 3. 乐观更新 (Optimistic Update)
    VM->>VM: 更新 UI State (清空输入框, 显示 Loading)
    VM-->>UI: StateFlow Emit (新状态)
    UI-->>User: UI 立即刷新 (乐观模式)

    %% 4. 业务逻辑
    VM->>Repo: 调用 sendMessage(text)

    %% 5. 持久化用户消息
    Repo->>DB: Insert: 用户消息
    
    %% 6. 网络请求
    Repo->>Net: streamChat(apiKey, text)
    activate Net

    %% 7. 流式循环
    loop SSE Stream (打字机效果)
        Net-->>Repo: Emit: 文本片段 ("Hel")
        Repo-->>VM: Flow Emit: 文本片段 ("Hel")
        VM->>VM: 更新 UI State (追加 "Hel")
        VM-->>UI: StateFlow Emit
        UI-->>User: 看到 AI 正在打字...
    end
    
    Net-->>Repo: 流结束
    deactivate Net

    %% 8. 持久化完整回复
    Repo->>DB: Insert: AI 完整回复
    
    %% 9. 最终状态
    Repo-->>VM: Flow 完成
    VM->>VM: 更新 UI State (Loading=false)
    VM-->>UI: StateFlow Emit
```

---

## 📂 项目结构

* **[`/composeApp`](./composeApp/src)**: 核心共享模块。
    * `commonMain`: 共享的 UI, ViewModels, 数据库, 和 API 客户端逻辑。
    * `androidMain`: Android 特定实现。
    * `iosMain`: iOS 特定实现。
    * `desktopMain`: 桌面端 (JVM) 特定实现。

* **[`/iosApp`](./iosApp/iosApp)**: iOS 入口点。
    * 包含 Xcode 项目和用于托管 Compose 内容的 SwiftUI 包装器。

---
*Built with ❤️ using [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/).*