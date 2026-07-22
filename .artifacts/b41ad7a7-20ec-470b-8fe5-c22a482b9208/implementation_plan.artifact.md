# Implementation Plan - Fix Build and SplashScreen Errors

The user reported two main issues:
1.  `Daemon compilation failed: Unknown or invalid session 1`: This is caused by Kotlin daemon instability, often due to memory limits or environment mismatches.
2.  `installSplashScreen showing error`: This appeared to be an "Unresolved reference" in the IDE, likely due to a sync issue or stale dependency.

## User Review Required

> [!IMPORTANT]
> I have already:
> - Stopped stale Gradle daemons.
> - Fixed syntax errors in `app/build.gradle.kts` that were preventing the project from compiling.
> - Performed a Gradle Sync which resolved the "Unresolved reference" for `installSplashScreen` in my analysis.

## Proposed Changes

### Build Configuration

#### [MODIFY] [gradle.properties](file:///Users/mac/AndroidStudioProjects/MyApplication/gradle.properties)
- Add `kotlin.daemon.jvmargs=-Xmx2g` to stabilize the Kotlin compiler daemon.

#### [MODIFY] [libs.versions.toml](file:///Users/mac/AndroidStudioProjects/MyApplication/gradle/libs.versions.toml)
- Upgrade `coreSplashscreen` from `1.0.1` to `1.2.0` to ensure compatibility with recent SDKs and AGP.

### UI / Activity

#### [MODIFY] [MainActivity.kt](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/java/com/islamic/asmaulhusna/MainActivity.kt)
- Update the `installSplashScreen` import and usage to be more explicit, which can help resolve persistent IDE "red underlines" even when the code compiles.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify the build remains successful with the new dependency version and memory settings.

### Manual Verification
- The user should check if the red underline on `installSplashScreen` in `MainActivity.kt` has disappeared. If it persists, a **File > Invalidate Caches...** in Android Studio is recommended.
