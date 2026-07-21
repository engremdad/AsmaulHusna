// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // On the classpath so :app can apply it conditionally once google-services.json exists.
    alias(libs.plugins.google.services) apply false
}