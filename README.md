# eChat 🤖

![Status](https://img.shields.io/badge/Status-Active%20Development-orange) ![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green)

**English** | [中文](./README_CN.md)

**eChat** is a cross-platform AI chat client built with **Kotlin Multiplatform (KMP)**. It serves as a unified interface for multiple LLM providers, allowing users to **Bring Your Own Keys (BYOK)** to chat with top-tier models like DeepSeek, Gemini, and OpenAI.

> 🚧 **Note:** This project is currently under **active development**. Some features are still being implemented. Feel free to open an issue if you encounter bugs!

## ✨ Key Features & Roadmap

- [x] **Multi-Provider Support:** Configure API keys for DeepSeek, Gemini, etc.
- [x] **Cross-Platform:** Runs natively on Android & iOS via KMP.
- [x] **Privacy First:** API keys and chats stored locally.
- [ ] **Chat History:** Persistent storage for conversations. (Coming Soon)
- [ ] **Markdown Support:** Better rendering for code blocks and tables. (Coming Soon)
- [ ] **Streaming Response:** Real-time typing effect.

---

## 🛠 Tech Stack & Architecture

This project is built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**.

**Why KMP?**
* **Native Performance:** Logic compiles directly to native binaries (JVM for Android, LLVM for iOS), ensuring zero runtime overhead.
* **Unified Development:** Shares **100% of business logic** (API Clients, ViewModel, DB) and **95%+ of UI** code.
* **Seamless Interop:** Full access to platform-specific APIs when needed.

### ⚖️ Cross-Platform Solution Comparison

Why we chose KMP over Flutter or React Native for eChat:

| Feature | **Kotlin Multiplatform (CMP)** | **Flutter** | **React Native** |
| :--- | :--- | :--- | :--- |
| **Performance** | **Native** (No Bridge/VM on iOS) | High (Dart VM + Custom Engine) | Good (JS Bridge overhead) |
| **UI Rendering** | **Skia (Canvas)** / Native Fallback | Skia / Impeller (Custom) | Native Components via JS |
| **Logic Sharing** | **100% Shared** (Networking, SQL) | Shared (Dart) | Shared (JS/TS) |
| **Ecosystem** | Reuse Android/Kotlin libraries | Dart-specific ecosystem | NPM / JavaScript ecosystem |

---

## 📂 Project Structure

* **[`/composeApp`](./composeApp/src)**: The core shared module.
    * `commonMain`: Shared UI, ViewModels, and API client logic (Ktor).
    * `androidMain`: Android specific implementation.
    * `iosMain`: iOS specific implementation.

* **[`/iosApp`](./iosApp/iosApp)**: The iOS entry point.
    * Contains the Xcode project and SwiftUI wrapper to host the Compose content.

## 🚀 Getting Started

### Android

To build and run the application on Android:
* Select the `composeApp` configuration in Android Studio.
* Or run via terminal:
    ```shell
    ./gradlew :composeApp:assembleDebug
    ```
  *(Windows: use `gradlew.bat`)*

### iOS

To build and run the application on iOS:
* Open **[`/iosApp`](./iosApp)** in Xcode.
* Or use the **Kotlin Multiplatform Mobile** plugin configuration in Android Studio.

---
*Built with ❤️ using [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/).*