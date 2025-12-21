# Vibe Coding Prompts 🚀

This document logs the **complete prompt engineering history** used to build **eChat** from scratch.

**📚 How to use:**
These prompts are designed as **sequential milestones**. By copying and sending them to your AI coding assistant (e.g., DeepSeek, Claude, or Gemini) one by one, you can reproduce the entire development process of this application.

**📱 App Context:**
* **App:** eChat - A Cross-Platform AI Client.
* **Tech Stack:** Kotlin Multiplatform (KMP), Compose, Room, Koin.
* **Features:** Bring Your Own Keys (BYOK), Multi-LLM Support, Offline History.

---

### 🎨 Looking for UI Design Prompts?
The prompts used to generate the high-fidelity UI assets and wireframes (via Stitch/Figma AI) are documented separately.
👉 **[View UI/UX Generation Prompts](../ui_ux/prompt.md)**

---

## 🏗️ Phase 1: Architecture Context (Architecture Warm-up)

**💡 Usage:**
Send this context *before* writing any code. It establishes the project's "World View," architecture (MVI), and technical constraints.

```text
[ROLE]
You are a Senior Kotlin Multiplatform (KMP) Mobile Developer. We are building "eChat", a cross-platform AI chat application allowing users to Bring Your Own Keys (BYOK).

[TECH STACK & ARCHITECTURE - STRICT ADHERENCE]
- Platform: Kotlin Multiplatform (Android & iOS).
- UI Framework: Compose Multiplatform (CMP) - Material3.
- Architecture: MVI (Model-View-Intent).
    - View: Compose UI (Stateless functions).
    - ViewModel: androidx.lifecycle.ViewModel holding StateFlow<ChatUiState>.
    - Intent: User actions defined as sealed interface ChatIntent.
    - State: Single source of truth defined as data class ChatUiState.
- Dependency Injection: Koin (for KMP & Compose).
- Local Data: Room Database (KMP, SQLite Bundled) for history; DataStore for settings.
- Network: Ktor Client (ContentNegotiation, Serialization) with Strategy Pattern for LLM providers.
- Project Structure:
    - Logic & UI must reside in "composeApp/src/commonMain".
    - Platform-specific code uses expect/actual only when necessary.

[CODE STYLE & DOCUMENTATION RULES]
1. Documentation: Add clear, concise comments to explain:
    - Key architectural decisions (e.g., why a specific MVI reducer logic is used).
    - Complex data transformations (especially in Repository/ViewModel layers).
    - Platform-specific hacks or "expect/actual" implementation details.
2. Use functional programming style where appropriate.
3. Prioritize "commonMain" implementation.
4. Keep UI components small and reusable.
5. Follow Clean Architecture: UI -> ViewModel -> Repository -> Data Source.

Please acknowledge that you understand this architecture. Do not generate code yet.
```

---

## 📋 Phase 2: UI Scaffolding (UI Implementation)

**💡 Usage:**
1.  Drag & Drop the UI screenshots (Empty State, Drawer, Chat History) into the chat.
2.  Send the prompt below to generate the basic UI code.

```text
[TASK]
Implement UI Scaffolding based on Wireframes.
I have attached screenshots of the desired UI. Please generate the Kotlin Multiplatform (Compose) code for the "ChatScreen" adhering to the previously agreed MVI architecture.

[VISUAL REQUIREMENTS (BASED ON IMAGES)]
1. Main Screen States
   The screen has two distinct states based on "uiState.messages.isEmpty()":
   
   State A: Empty / New Chat (Ref: screen.png)
   - Layout: Centered vertical content.
   - Model Selector (Crucial): A Vertical Scrollable Picker (Wheel style) located in the center.
     - Interaction: Users swipe up/down to select the model. It must have snapping behavior (snaps to the center item).
     - Visual Order: 
       - Top: Gemini (faded/smaller)
       - Center: DeepSeek (Selected, fully visible, larger logo)
       - Bottom: OpenAI (faded/smaller)
   - Greeting: "How can I help you today?" text below the picker.
   - Bottom Bar: The input field is always visible.

   State B: Active Chat (Ref: chat_history.png)
   - Content: A "LazyColumn" displaying the message history.
   - Styling:
     - User Bubble: Right-aligned, light background.
     - AI Bubble: Left-aligned, includes an Avatar/Icon, transparent/gray background.
   - TopAppBar: Shows the currently selected model name (e.g., "ChatGPT 4").

2. Navigation Drawer (Ref: drawer.png)
   - Header: "New Chat" button.
   - History List: Grouped by time periods (e.g., "Today", "Yesterday", "Previous 7 Days").
   - Footer: User profile section (Avatar + Name) at the very bottom.

3. Input Component (Ref: screen.png / chat_history.png)
   - Rounded corner shape (Capsule style).
   - "Plus" (+) icon on the left.
   - "Up Arrow" (Send) icon on the right (filled circle background).

[TECHNICAL CONSTRAINTS]
- File: "commonMain/kotlin/.../ui/ChatScreen.kt"
- State Management: Use "when" or "if" to switch between "EmptyChatView" and "MessageListView" inside the Scaffold content.
- Picker Implementation: Use "VerticalPager" (with pageSize/contentPadding) or "LazyColumn" with "rememberSnapFlingBehavior" to achieve the wheel picker effect.
- Comments: Add comments explaining the picker logic and state switching.

[OUTPUT]
Generate the full "ChatScreen.kt" and "ChatViewModel.kt" (with mock data to demonstrate the "Empty" vs "Active" state switching).
```

---

## 🗄️ Phase 3: Local Database (Room KMP)

**💡 Usage:**
Configures the Room Database. Ensure the AI strictly follows the version numbers for KMP compatibility.

```text
[TASK]
Set up Room Database for Kotlin Multiplatform (KMP) following the official Android KMP Room guide.
Goal: Build successfully on Android + iOS with bundled SQLite driver.

[CRITICAL CONSTRAINTS]
- NO DI / NO Repository pattern yet.
- Only do Gradle config + Entities + DAOs + Database + Platform builders.
- Android Context can be passed in manually or via a simple static helper.

[STRICT VERSIONS]
- Kotlin 2.1.0 (or current stable)
- KSP = (Matching Kotlin version)
- Room 2.7.0-alpha (or latest KMP stable)
- SQLite bundled 2.5.0-alpha (or latest KMP stable)

[STEP 1: GRADLE CONFIG]
Provide the exact changes for libs.versions.toml, root build.gradle.kts, and composeApp/build.gradle.kts.
Requirements:
- Apply KSP plugin (com.google.devtools.ksp).
- Add dependencies:
  - androidx.room:room-runtime (commonMain)
  - androidx.sqlite:sqlite-bundled (commonMain)
  - androidx.room:room-compiler via KSP for all targets (kspCommonMainMetadata, kspAndroid, kspIosArm64, kspIosSimulatorArm64).
- If not exporting schemas, set exportSchema = false in @Database (no Room Gradle plugin required).

[STEP 2: DATABASE SCHEMA]
Define in commonMain:
- SessionEntity: id (String, PK), title (default "New Chat"), createdAt (Long), modelProvider (String)
- MessageEntity: id (String, PK), sessionId (FK -> SessionEntity, cascade delete), role (String), content (String), timestamp (Long), status (String), modelProvider (String?)
- Create SessionDao and MessageDao (basic queries + insert + delete).

[STEP 3: ROOM KMP CONSTRUCTOR]
- Add @ConstructedBy(AppDatabaseConstructor::class) on AppDatabase.
- Declare in commonMain:
  expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
- Do not write actual AppDatabaseConstructor (KSP generates it).

[STEP 4: PLATFORM DATABASE BUILDERS]
- commonMain: expect class DatabaseFactory { fun create(): AppDatabase }
- Android actual: use Room.databaseBuilder(context, AppDatabase::class.java, "echat.db")
- iOS actual: use Room.databaseBuilder<AppDatabase>(name = "<writable path>/echat.db")
  - Use Documents or Application Support directory from Foundation.
  - Set BundledSQLiteDriver().

[STEP 5: OUTPUT]
Generate the Kotlin code for:
1. Gradle config changes
2. Entities + DAOs + AppDatabase
3. DatabaseFactory expect/actual for Android + iOS
Only output these files and changes.
```

---

## 💉 Phase 4: DI & Repo (Koin & Repository)

**💡 Usage:**
Integrates Dependency Injection. Specifically targets iOS compatibility issues (SavedStateHandle) and Repository pattern implementation.

```text
[TASK]
Integrate Koin for Dependency Injection and implement the Repository Pattern to connect the UI (ViewModel) with the Local Database (Room).
Goal: Replace mock data in ViewModel with real database data, fix Android Context injection, and ensure iOS stability by avoiding SavedStateHandle.

[CRITICAL CONSTRAINTS - iOS COMPATIBILITY]
1. NO SavedStateHandle: Do NOT inject SavedStateHandle into ChatViewModel. Do NOT use koin-androidx-compose-navigation.
2. Simple Injection: Use standard constructor injection for the ViewModel.
3. Linkage Safety: Use basic koin-compose APIs to avoid IrLinkageError on iOS Native targets.

[STEP 1: KOIN DEPENDENCIES]
Add strict Koin dependencies to libs.versions.toml and build.gradle.kts (commonMain):
- io.insert-koin:koin-core
- io.insert-koin:koin-compose (This provides koinViewModel() for CMP).
- io.insert-koin:koin-android (androidMain only).

[STEP 2: KOIN MODULES STRUCTURE]
- PlatformModule (expect/actual val):
  - Common: expect val platformModule: Module
  - Android (androidMain): actual val platformModule = module { single { getDatabaseBuilder(androidContext()) } }
    - Fix: Ensure getDatabaseBuilder (from Phase 3) is either refactored to take Context or handled within the module using androidContext().
  - iOS (iosMain): actual val platformModule = module { single { getDatabaseBuilder() } }

- AppModule (commonMain):
  - Define val appModule = module { ... }.
  - Include platformModule.
  - Provide AppDatabase (create using the builder).
  - Provide ChatDao & SessionDao (get from AppDatabase).
  - Provide ChatRepository (Single).
  - Provide ChatViewModel: Use viewModelOf(::ChatViewModel) or viewModel { ChatViewModel(get()) }.

[STEP 3: REPOSITORY IMPLEMENTATION]
Create ChatRepository in commonMain:
- Dependencies: Inject SessionDao, MessageDao.
- Logic:
  - getHistory(): Return sessionDao.getAllSessions().
  - getMessages(sessionId): Return messageDao.getMessages(sessionId).
  - sendMessage(text, model):
    - Check activeSessionId. If null, create new SessionEntity & save modelProvider.
    - Insert User MessageEntity.
    - Mock AI: Insert a "Echo: $text" response after 1s delay (using delay(1000)).
  - loadSession(id) & newChat(): Manage active session state.

[STEP 4: VIEWMODEL REFACTORING]
Refactor ChatViewModel to use ChatRepository:
- Constructor: class ChatViewModel(private val repository: ChatRepository) : ViewModel()
- Init: Collect repository.messages and repository.activeSession.
- Actions: Forward NewChat, LoadSession, SendMessage to Repository.
- State: Map Repository flows to ChatUiState.

[STEP 5: INITIALIZATION]
- Android (MainApplication.kt): startKoin { androidContext(this@MainApplication); modules(appModule) }
- iOS (Helper.kt): fun initKoin() { startKoin { modules(appModule) } }
- iOS (iOSApp.swift): Call initKoin() on startup.

[OUTPUT]
Generate Kotlin code for:
1. build.gradle.kts (Dependencies).
2. PlatformModule.kt (expect/actual) - Focus on Android Context fix.
3. AppModule.kt (No SavedStateHandle).
4. ChatRepository.kt.
5. ChatViewModel.kt (Clean constructor).
6. Helper.kt (iOS init).
```

---

## 🌐 Phase 5: Networking & LLM Strategy

**💡 Usage:**
Implements the real Network layer using Ktor. Enforces the **Strategy Pattern** to decouple OpenAI, DeepSeek, and Gemini implementations.

```text
[TASK]
Implement the Networking layer using Ktor Client to connect to real LLM APIs.
Goal: Replace the mock AI response in ChatRepository with real network calls.
Architecture: Use the Strategy Pattern with strict separation of both Service classes AND Data Models (DTOs) for OpenAI, DeepSeek, and Gemini.

[STEP 1: DEPENDENCIES]
Add Ktor and Serialization to libs.versions.toml and build.gradle.kts (commonMain):
- Plugins: kotlinx.serialization
- Dependencies:
  - io.ktor:ktor-client-core, io.ktor:ktor-client-content-negotiation, io.ktor:ktor-serialization-kotlinx-json
  - io.ktor:ktor-client-okhttp (androidMain)
  - io.ktor:ktor-client-darwin (iosMain)
- Android Manifest: Ensure <uses-permission android:name="android.permission.INTERNET" /> is added.

[STEP 2: API STRATEGY PATTERN (Strict Decoupling)]
Create the LLM architecture in commonMain:

1. Interface LLMService:
   - suspend fun generateResponse(messages: List<MessageEntity>): String
   - This is the ONLY shared abstraction.

2. Implementation A: OpenAI Ecosystem
   - File OpenAIModels.kt: Define OpenAIRequest, OpenAIMessage, OpenAIResponse data classes.
   - File OpenAIService.kt:
     - Constructor: (client: HttpClient, apiKey: String)
     - Base URL: https://api.openai.com/v1
     - Logic: Map MessageEntity -> OpenAIRequest, call API, return content.

3. Implementation B: DeepSeek Ecosystem
   - File DeepSeekModels.kt: Define DeepSeekRequest, DeepSeekMessage, DeepSeekResponse data classes.
   - Note: Even if these look identical to OpenAI's models right now, DUPLICATE THEM. Do not reuse OpenAI models. This ensures future extensibility.
   - File DeepSeekService.kt:
     - Constructor: (client: HttpClient, apiKey: String)
     - Base URL: https://api.deepseek.com
     - Logic: Map MessageEntity -> DeepSeekRequest, call API, return content.

4. Implementation C: Gemini Ecosystem
   - File GeminiModels.kt: Define GeminiRequest, GeminiContent...
   - File GeminiService.kt: Handle Google's specific JSON structure.

5. Factory: LLMFactory
   - getService(modelProvider: String): LLMService
   - Switch logic:
     - "DeepSeek" -> returns DeepSeekService
     - "OpenAI" -> returns OpenAIService
     - "Gemini" -> returns GeminiService

[STEP 3: HTTP CLIENT SETUP (KOIN)]
Update AppModule.kt:
- Provide HttpClient (ContentNegotiation/Json).
- Define placeholder API Keys (OPENAI_KEY, DEEPSEEK_KEY, GEMINI_KEY).
- Provide LLMFactory (Singleton).

[STEP 4: REPOSITORY INTEGRATION]
Update ChatRepository.kt:
- Inject LLMFactory.
- Refactor sendMessage(text, model):
  - Save User Message (Locally).
  - Strategy: val service = llmFactory.getService(currentSession.modelProvider)
  - Network: val responseText = service.generateResponse(...)
  - Save AI Message (Locally).
  - Error Handling: Try-catch block -> Save Error Message entity on failure.

[OUTPUT]
Generate Kotlin code for:
1. build.gradle.kts (Dependencies).
2. LLMService.kt (Interface) & LLMFactory.kt.
3. OpenAIModels.kt & OpenAIService.kt.
4. DeepSeekModels.kt & DeepSeekService.kt (Separate file, separate DTOs).
5. GeminiModels.kt & GeminiService.kt.
6. AppModule.kt (Koin setup).
7. Updated ChatRepository.kt.
```

---

## ⚙️ Phase 6: DataStore & Settings (UX Logic)

**💡 Usage:**
Adds API Key persistence via DataStore and implements the "Smart UX" that prevents chatting if keys are missing.

```text
[TASK]
Implement Jetpack DataStore for API Key persistence, create a Settings Screen with Password Visibility Toggles, and implement Smart UX Logic in the Chat Screen.
Goal: Persist API Keys, guide the user via dynamic UI states, and allow users to toggle API Key visibility (show/hide) in the Settings screen to prevent typos.

[STEP 1: DEPENDENCIES]
Add androidx.datastore:datastore-preferences-core to libs.versions.toml and build.gradle.kts (commonMain).

[STEP 2: DATASTORE LAYER]
- Factory: Create fun createDataStore(producePath: () -> String): DataStore<Preferences>.
- PlatformModule: Inject DataStore using context.filesDir (Android) and NSFileManager (iOS).
- SettingsRepository:
  - saveApiKey(provider, key)
  - getApiKey(provider)
  - val settingsFlow: Exposes a UserSettings object (holding all 3 keys).

[STEP 3: VIEWMODEL LOGIC (Smart State)]
Refactor ChatViewModel:
- Observe Settings: Collect settingsRepository.settingsFlow.
- Computed Property: Add val isKeyMissing: Boolean to ChatUiState.
  - Logic: True ONLY if the currently selected model's key is blank.
- Send Interception:
  - In sendMessage(): Check if (uiState.value.isKeyMissing).
  - Action: If missing, DO NOT trigger the repository. Emit a UI event or Snackbar.

[STEP 4: UI IMPLEMENTATION (Settings with Toggle)]
SettingsScreen.kt:
- Layout: A Column with Header ("Settings") and Back button.
- Input Fields: 3 OutlinedTextFields (OpenAI, DeepSeek, Gemini).
- Crucial Feature (Password Toggle):
  - For each text field, manage a local var isPasswordVisible by remember { mutableStateOf(false) }.
  - VisualTransformation: Switch between VisualTransformation.None (if visible) and PasswordVisualTransformation() (if hidden).
  - Trailing Icon: Use an IconButton. Icon: Icons.Filled.Visibility (if hidden) vs Icons.Filled.VisibilityOff (if visible).
  - OnClick: Toggle the boolean state.
- Action: "Save" button.

[STEP 5: UI IMPLEMENTATION (Chat Screen UX)]
ChatScreen.kt:
- Dynamic Empty Text:
  - Below Model Picker.
  - Logic: IF isKeyMissing: "⚠️ API Key missing. Tap to configure." (Clickable -> Navigate). ELSE: "How can I help you today?".
- Send Button: Intercept click if key is missing.

[OUTPUT]
Generate Kotlin code for:
1. build.gradle.kts (Deps).
2. PlatformModule.kt & SettingsRepository.kt.
3. ChatViewModel.kt (Smart Logic).
4. ChatScreen.kt (Focus on Clickable Text & Interception).
5. SettingsScreen.kt (Focus on the Password Visibility Logic).
```

---

## 🎨 Phase 7: Markdown & Polish

**💡 Usage:**
The final polish. Introduces Markdown rendering for rich AI responses (code blocks, bold text, etc.).

```text
[TASK]
Enhance the Chat UI by integrating Markdown Rendering support.
Goal: Replace the raw text display in chat bubbles with a proper Markdown renderer to support bolding, headers, lists, and code blocks with background styling.

[STEP 1: DEPENDENCIES]
Add the multiplatform-markdown-renderer library to libs.versions.toml and build.gradle.kts (commonMain):
- Library: com.mikepenz:multiplatform-markdown-renderer
- Version: 0.27.0 (or latest compatible).
- Repo: Ensure mavenCentral() is present.

[STEP 2: UI REFACTORING (MessageBubble)]
Update ChatScreen.kt (specifically the MessageBubble composable):
- Conditional Rendering:
  - User Messages: Keep using standard Text (users rarely type complex markdown).
  - AI Messages: Use Markdown(content) composable from the library.
- Styling (Crucial for "Look & Feel"):
  - Apply MaterialTheme.typography.bodyLarge (or similar) to the Markdown content.
  - Colors: Ensure the Markdown text color matches MaterialTheme.colorScheme.onSurface (or onPrimaryContainer depending on bubble background).
  - Code Blocks: The library handles code blocks by default. Ensure the code block background color contrasts slightly with the bubble color (e.g., if bubble is white, code block is light gray).

[STEP 3: AESTHETICS TWEAKS]
- Increase lineHeight slightly for better readability.
- Ensure the Markdown component fills the available width of the bubble but respects padding.

[OUTPUT]
Generate Kotlin code for:
1. libs.versions.toml & build.gradle.kts.
2. Updated ChatScreen.kt (Showing the updated MessageBubble that switches between Text/Markdown).
```