# ── Asma al-Husna R8/ProGuard rules ──────────────────────────────
# R8 is not yet enabled (buildTypes.release.optimization.enable = false).
# These keeps are staged so enabling shrinking later is safe.

-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod

# Firebase Cloud Messaging service must survive shrinking (instantiated by the framework).
-keep class com.islamic.asmaulhusna.notify.AsmaulHusnaMessagingService { *; }
