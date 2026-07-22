# ── Asma al-Husna R8/ProGuard rules ──────────────────────────────
# R8 code + resource shrinking is ENABLED for release (isMinifyEnabled /
# isShrinkResources in app/build.gradle.kts). Firebase and Compose ship their
# own consumer rules; these are the app-specific keeps on top.

-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod

# Firebase Cloud Messaging service must survive shrinking (instantiated by the framework).
-keep class com.islamic.asmaulhusna.notify.AsmaulHusnaMessagingService { *; }
