# Easy Estate Android

Starter Android app for Easy Estate written in Kotlin. The project is set up with Jetpack Compose and Material 3 so you can begin iterating right away.

## Requirements

- Android Studio Iguana (or newer)
- Android SDK 34 installed via the SDK Manager
- JDK 17 (bundled with recent Android Studio versions)

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Lipkariuki/easy-estate-android.git
   cd easy-estate-android
   ```
2. Open the project in Android Studio (`File` → `Open` → select the project folder).
3. Let Gradle download dependencies and sync the project when prompted.
4. Connect an Android device or start an emulator from the Device Manager.
5. Run the app using the **Run** button in Android Studio or with:
   ```bash
   ./gradlew installDebug
   ```

The home screen shows a simple welcome message rendered with Jetpack Compose. Replace the `GreetingScreen` content in `app/src/main/java/com/easyestate/android/MainActivity.kt` with your actual UI as you progress.

### Configuring the API base URL

Networking defaults to `http://10.0.2.2:8000/`, which points to the host machine when running inside an Android emulator.  
When testing on a physical device, override the base URL at build time so the app can reach your backend:

1. Make sure the device and the backend are on the same network and note the machine’s IP (e.g. `192.168.1.163`).
2. Set the Gradle property before building, either by adding a line to `local.properties` / `~/.gradle/gradle.properties`:
   ```
   API_BASE_URL=http://192.168.1.163:8000/
   ```
   or by supplying it on the command line:
   ```bash
   ./gradlew assembleDebug -PAPI_BASE_URL=http://192.168.1.163:8000/
   ```
3. Reinstall the newly built APK on the device.

The value is exposed to the app via `BuildConfig.API_BASE_URL`, and the client falls back to `10.0.2.2` when none is provided.

## Demo Account

To explore the prototype screens quickly, use the seeded admin credentials:

- Email: `admin@easyestate.com`
- Password: `Admin123!`

Successful sign-in takes you to a static home dashboard inspired by the shared mockups.

## Project Structure

- `app` — main Android application module
  - `src/main/java` — Kotlin source (including Compose UI)
  - `src/main/res` — Android resources (strings, themes, etc.)
  - `src/test` and `src/androidTest` — unit and instrumentation tests

Feel free to add new modules (e.g., `core`, `feature-*`) as the app grows.
