# Ancient Wisdom Oracle

A minimalist Android application designed to offer a moment of reflection and guidance through a digital interpretation of traditional Chinese divination practices. This project is a solo endeavor, built from the ground up to demonstrate a complete product development lifecycle, from concept to a fully functional app.

<!-- Add a screenshot or GIF of the main shake screen here! It will make your README much more attractive. -->
![App Screenshot Placeholder](https://via.placeholder.com/300x600.png?text=App+Screenshot+Here)

## The Soul of the Product: A Meditative Journey

In a world of instant answers, the Ancient Wisdom Oracle is designed to be different. It doesn't just provide a result; it guides the user through a brief, meditative journey. The core philosophy is that sincerity and patience are part of the process of seeking wisdom.

This is reflected in the app's unique features:

*   **The Ritual:** The app intentionally slows the user down with a calming incense-burning animation, creating a space for intention and reflection before the divination begins.
*   **Failure as a Path to Wisdom:** Unlike other apps, a "failed" attempt is not a dead end. It is a feature. The app presents the user with an ancient aphorism, encouraging another try.
*   **The Dao De Jing Gate:** True to the spirit of Taoist philosophy, persistent "failure" (three consecutive unsuccessful attempts) is not a punishment, but an opportunity. The user is gently guided to a chapter of the Dao De Jing, suggesting that perhaps the answer they seek lies not in a simple oracle, but in deeper philosophical contemplation. This entire loop is a deliberate design choice to embody the product's soul.

## Key Features

*   **Ritualistic Divination Flow:** A complete, multi-step user journey including a user agreement, a guided introduction, a pre-ceremony incense ritual, and the core shake-to-draw mechanic.
*   **Intuitive Shake Mechanic:** Utilizes the device's accelerometer (`SensorManager`) to detect a physical shake, connecting the user's physical action to the digital outcome. A manual button is also available.
*   **Complex Outcome Logic:**
    *   **Success:** A successful draw (50% chance) leads directly to a unique oracle result.
    *   **Failure:** An unsuccessful draw prompts the user with an ancient aphorism and encourages a retry.
    *   **Persistent Failure:** After three consecutive failures, the app redirects the user to the Dao De Jing reader, resetting the failure counter thereafter.
*   **Integrated Dao De Jing Reader:** Includes the full 81 chapters of the Dao De Jing for reading and contemplation.
*   **State Persistence:** The app intelligently uses `SharedPreferences` to remember the user's agreement status and the count of failed attempts, ensuring a seamless experience across sessions.
*   **Immersive Experience:** Incorporates background music, sound effects, and video animations to create a rich, immersive user experience.

## Technical Stack & Architecture

*   **Language:** **Java**
*   **Platform:** **Native Android**
*   **Architecture:** A simple, state-driven architecture was chosen for rapid development and clarity. The application flow is managed through `Activity` lifecycles and explicit `Intents`.
*   **UI:**
    *   Standard Android XML layouts.
    *   `VideoView` is used to render immersive animations for the incense and shake ceremonies.
    *   Custom `TypewriterTextView` for a dynamic text-reveal effect.
*   **Hardware Integration:** `SensorManager` and `Sensor.TYPE_ACCELEROMETER` are used to implement the core shake-detection feature.
*   **State Management:** `SharedPreferences` are used for lightweight and persistent state management (e.g., user agreement, failure counts).
*   **Code Structure:** A `BaseActivity` is utilized to handle common functionalities like sound and vibration management, promoting code reuse.

## Getting Started

This project is self-contained and can be built directly using Android Studio.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/zzy1987z-netizen/ancient-wisdom-oracle.git
    ```
2.  **Open in Android Studio:**
    Select `File > Open` and navigate to the cloned project directory.
3.  **Gradle Sync:**
    Allow Android Studio to automatically sync the project with the Gradle files.
4.  **Build & Run:**
    Run the app on an emulator or a physical Android device.

## About the Developer

This project represents a solo journey into the world of Android development. As a self-taught developer, every aspect of this application—from the initial concept and UX flow to the final code implementation and asset integration—was handled independently. It demonstrates a strong passion for learning and the ability to deliver a complete, thoughtful product from scratch.
