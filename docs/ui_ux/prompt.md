# UI/UX Generation Prompts 🎨

This document records the prompt engineering history used to generate the high-fidelity UI assets for eChat using AI tools ([Stitch](https://stitch.withgoogle.com) / Figma AI).

![Overview](./ui_ux.png)

## 1. Chat Config / Empty State (API Key Missing)

**Goal:** Create a clean startup screen that guides the user to configure their API key while showing the model selector.

> Mobile app UI design for an LLM AI chat application, high fidelity.
>
> **Scene:**
> A blank page for starting a new conversation, displaying an API key missing error.
>
> **Style:**
> Clean, minimalist, modern, Apple Human Interface Guidelines mixed with Material Design 3.
>
> **Color Palette:**
> Soft white background, slate gray for unselected model text, dark gray for input placeholder and disclaimer text, a modern high-end green for the send button. The error message uses a high-end, modern red font.
>
> **Details:**
> * **Header:** Features a minimalist hamburger menu icon on the top-left, and minimalist outline-based edit and settings gear icons on the top-right.
> * **Central Area:** Displays a vertical, scrollable model selector with a 'date picker' effect. 'ChatGPT 4o' is prominently displayed as the centered, selected model with its lightning icon to the left. 'Gemini Advanced' is visible above, and 'DeepSeek V3' below, both appearing smaller and desaturated.
> * **Error State:** The text "API Key missing. Tap to configure." is displayed prominently in the specified modern red font, below the model picker, and centered on a single line.
> * **Input Area:** The bottom input area is a floating, rounded text input field with placeholder text "Message...", an appropriate height, and a prominent, modern high-end green send button (up arrow icon).
> * **Disclaimer:** A small disclaimer text "AI can make mistakes. Check important info." is present below the input field in a lighter, subtle gray color.
>
> **Vibe:**
> Professional, airy, easy to read, Dribbble trending, clear error indication.

---

## 2. API Key Settings Page

**Goal:** A secure and clear configuration page for managing multiple provider keys.

> Mobile app UI design for an LLM AI chat application, high fidelity.
>
> **Scene:**
> An API Key configuration page.
>
> **Style:**
> Clean, minimalist, modern, Apple Human Interface Guidelines mixed with Material Design 3.
>
> **Color Palette:**
> Soft white background (#F8F8F8), slate gray text (#555555), primary accent color is a modern, high-end green for the save button (#28A745).
>
> **Details:**
> * **Header:** The screen has a simple top bar with a left-aligned back arrow icon and a centered title "API Configuration".
> * **Description:** Below the header is a descriptive text: "Configure your AI model providers. Keys are stored locally on your device."
> * **Main Content:** The main content area features three distinct, rounded white cards for API key configuration. Each card has a model logo (DeepSeek, OpenAI, Gemini) and title (e.g., "DeepSeek", "OpenAI", "Gemini 1.5 Pro & Flash"), along with a subtitle describing the model capabilities.
> * **Input Fields:** Inside each card, there is a floating, light gray input field with a padlock icon on the left, displaying obscured API key text (dots or partial key like "sk-..." or "Alza...").
>     * **DeepSeek Card:** Includes a green checkmark icon on the top right, and the input field has an eye-slash icon to toggle visibility.
>     * **OpenAI Card:** The input field has an eye icon.
>     * **Gemini Card:** The input field has a "Paste" button on the right.
> * **Links:** There are no "Get API Key" links.
> * **Action Button:** A floating, large, rounded rectangular button at the bottom, colored in a modern, high-end green, reads "Save Changes" with a white checkmark icon on the right.
>
> **Vibe:**
> Professional, airy, easy to read, Dribbble trending.

---

## 3. Active Chat Interface

**Goal:** Visualize the core chat experience, focusing on message bubble styling and code block rendering.

> Mobile app UI design for an LLM AI chat application, high fidelity.
>
> **Scene:**
> An active conversation screen between a user and an AI.
>
> **Style:**
> Clean, minimalist, modern, Apple Human Interface Guidelines mixed with Material Design 3.
>
> **Color Palette:**
> Soft white background, slate gray text, primary accent color is a modern, high-end green.
>
> **Details:**
> * **Chat Bubbles:** Rounded corners, subtle drop shadows. User bubbles are a modern, high-end green (no avatar), AI bubbles are light gray (no avatar).
> * **Content:** The AI response includes a clean code block snippet with syntax highlighting (dark mode code block on light theme). The code block header has traffic light dots (red, yellow, green) and a "fetch_data.py" title, with a "Copy" button on the right.
> * **Header:** A clean top bar with a centered model name "DeepSeek V3", a minimalist hamburger menu icon (left), and minimalist outline-based edit and settings gear icons (right). A date/time stamp "TODAY 9:41 AM" is centered below the header.
> * **Input Area:** A floating rounded text input field at the bottom with placeholder text "Message DeepSeek...", an appropriate height, and a prominent, modern high-end green send button (up arrow icon).
> * **Disclaimer:** A small disclaimer text "AI can make mistakes. Please verify important information." is present below the input field in a lighter, subtle color.
>
> **Vibe:**
> Professional, airy, easy to read, Dribbble trending.

---

## 4. AI Chat Select Model

**Goal:** Design the main landing screen where users select their preferred LLM before starting a chat.

> Mobile app UI design for an LLM AI chat application, high fidelity.
>
> **Scene:**
> A blank page for starting a new conversation.
>
> **Style:**
> Clean, minimalist, modern, Apple Human Interface Guidelines mixed with Material Design 3.
>
> **Color Palette:**
> Soft white background, slate gray text for unselected models, dark gray for the main slogan and input placeholder, modern high-end green for the send button.
>
> **Details:**
> * **Header:** Features a minimalist hamburger menu icon on the top-left, and minimalist outline-based edit and settings gear icons on the top-right.
> * **Model Selector:** The central area displays a vertical, scrollable model selector with a 'date picker' effect. 'ChatGPT 4o' is prominently displayed as the centered, selected model with its lightning bolt icon to the left. 'Gemini Advanced' is visible above, and 'DeepSeek V3' below, both appearing smaller, desaturated, and with their respective icons.
> * **Slogan:** The slogan "How can I help you today?" is displayed below the model picker in a single line, centered, with a dark gray color.
> * **Input Area:** The bottom input area is a floating, rounded text input field with placeholder text "Message...", an appropriate height, and a prominent, modern high-end green send button (up arrow icon).
> * **Disclaimer:** A small disclaimer text "AI can make mistakes. Check important info." is present below the input field in a lighter, subtle gray color.
>
> **Vibe:**
> Professional, airy, easy to read, tranquil, Dribbble trending.

---

## 5. Navigation Drawer (Chat History)

**Goal:** Show how the app manages chat history and user navigation with a distinct visual hierarchy.

> Mobile app UI design for an LLM AI chat application, high fidelity.
>
> **Scene:**
> A left-sliding navigation drawer (sidebar) displaying chat history.
>
> **Style:**
> Clean, minimalist, modern, Apple Human Interface Guidelines mixed with Material Design 3.
>
> **Color Palette:**
> Soft white background, slate gray text, primary accent color is a modern, high-end green for accents and selected states, and specific colors for model indicators (green for DeepSeek, gray/black for OpenAI, orange for Gemini).
>
> **Details:**
> * **Background:** Features a subtle blur (frosted glass effect) over the main content, allowing it to be faintly visible.
> * **Top Action:** At the top, there is a prominent, rounded white "New Chat" button with a green plus icon.
> * **History List:** Chat history is organized by time dividers: "TODAY", "YESTERDAY", "PREVIOUS 7 DAYS" – these are smaller, in ALL CAPS, with increased letter spacing, and lighter gray color. Chat conversation titles (e.g., "Python API Script Helper", "React Component Optimization") are dark gray.
> * **Selected Item:** The selected chat item ("Python API Script Helper") is highlighted with a prominent, full-height vertical green bar on its left side.
> * **Model Indicators:** To the right of each chat item is a unique, composite model indicator (a quarter circle at the top-left joined by a square whose side equals the radius, both colored to represent the model, e.g., green for DeepSeek, black for OpenAI, orange for Gemini, here shown as green for the selected item).
> * **User Profile:** The bottom area contains a distinct, floating rounded white card for user information: a circular profile icon (with a green border), "Ethan Designer", and "example@email.com", with a three-dot menu icon on the right.
>
> **Vibe:**
> Professional, airy, easy to read, 'Memory Palace' feel, Dribbble trending.