# Weather_app

A simple Android weather application (Gradle/Kotlin DSL) with activities for Main, Favorites, History, Settings, and About. The project includes a `WeatherDatabase` and basic UI layouts under `app/src/main/res`.

## Features

- MainActivity: primary UI for showing weather information.
- FavoritesActivity: save and view favorite locations.
- HistoryActivity: view previously searched locations.
- SettingsActivity: app preferences and configuration.
- AboutActivity: app information.
- Local `WeatherDatabase` for persisting data.

> Project is organized using Gradle Kotlin DSL (build scripts with `.kts`).

## Repository structure (high-level)

- `app/` – Android application module
  - `src/main/java/com/example/weather_app/` – Activities and database class
  - `src/main/res/` – layouts, drawables, themes, and other resources
  - `build.gradle.kts` – module build file
- `build.gradle.kts`, `settings.gradle.kts` – top-level Gradle Kotlin DSL files
- `gradle/` – Gradle wrapper and version catalog

## Prerequisites

- Java JDK (11+ recommended)
- Android SDK (matched to project's compileSdk/targetSdk)
- Android Studio (recommended) or command line Gradle
- On Windows PowerShell (this repo's environment): use `gradlew` / `gradlew.bat` to run Gradle commands

## Build & Run (PowerShell)

Open a PowerShell prompt inside the project root and run:

```powershell
# Build debug APK
.\gradlew.bat assembleDebug

# Install debug APK to a connected device (replace <deviceId> if needed)
.\gradlew.bat installDebug

# Run unit tests
.\gradlew.bat test
```

Alternatively, import the project into Android Studio and use the IDE Run/Debug tools.

## Notes & Assumptions

- Package name: `com.example.weather_app` (see `app/src/main/java/...`).
- No external API keys or services are included in the repository by default.
- If you add API keys or secrets, do not check them into source control; use safer storage (Gradle properties, environment variables, or Android resource protection mechanisms).

## Contributing

Contributions are welcome. Please open issues or pull requests with a clear description of the change. Keep changes small and focused. Add or update unit tests where relevant.

## License

This project is licensed under the MIT License — see the `LICENSE` file for details.

