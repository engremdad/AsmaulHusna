# Google Play Store — Publishing Gap Analysis

_App: Asma al-Husna · `com.islamic.asmaulhusna` · version 1.0 (versionCode 1) · minSdk 24 · targetSdk 36_
_Last reviewed: 2026-07-22_

Status legend: 🔴 Blocker · 🟡 High-risk · 🟢 Minor polish · ✅ Resolved

---

## ✅ Resolved since the first analysis
- **Adaptive launcher icon** — Mushaf icon with foreground/background/monochrome layers (no longer the default robot).
- **Localization** — 7 languages (en/ar/bn/hi/in/tr/ur), English as the base `values/`, RTL support, and a first-run language screen. (Was "Bengali-only".)
- **Share/Copy** — full-text Share + Copy on the name detail screen.
- **Notification-permission guidance** — Settings shows a banner when notifications are blocked, deep-linking to system settings (added while fixing "reminders not firing").
- **Top-bar deprecation** — Home switched off `centerAlignedTopAppBarColors` to `topAppBarColors`.
- **Empty states** — Favorites has a proper empty state.
- **Audio blocker resolved** (#1) — the unlicensed GitHub hotlink is gone. All 99 pronunciation clips (Mohammed Sadiq / Wikimedia Commons / **CC BY-SA 4.0**) are now bundled in `res/raw/` and play fully offline; `AudioPlayer.ENABLED = true`. CC BY-SA attribution + licence link surfaced in **Settings › About › Audio credits**, with the notice file at `assets/licenses/asma_ul_husna_audio_attribution.txt`.
- **FCM token upload removed** (#2) — `TokenUploader` (which POSTed the device token to the `api.example.com` placeholder) is deleted, along with its retrofit/gson/coroutines deps. The FCM *receive* path is kept (`AsmaulHusnaMessagingService`); push is now targeted from the Firebase console / topics, so no device identifier is collected or sent anywhere.
- **Exact-alarm permission** (#7) — dropped entirely in favour of inexact, doze-friendly alarms (`setAndAllowWhileIdle`). Exact alarms are Play-restricted to core alarm/reminder apps; as a reference app we accept a few minutes' tolerance and avoid the restricted permission / Console declaration.
- **`VolumeUp` deprecation** (#15) — switched to `Icons.AutoMirrored.Filled.VolumeUp`.
- **Hard-coded Bangla toasts** (#19) — `AudioPlayer` toasts extracted to localized string resources.
- **`ahad.mp4` typo** (#22) — corrected to `.mp3`.
- **Backup rules** (#11) — explicit `backup_rules.xml` / `data_extraction_rules.xml` now include only the user's prefs (favorites, zikir, settings, reminders) and exclude Firebase id files. Verified the include paths match the real `SharedPreferences` filenames, so backup isn't silently empty. _Maintenance: if a new prefs file is added, add it to both rule files or it won't be backed up._
- **Splash screen** (#13) — Android 12+ SplashScreen API added (gold Mushaf mark on the emerald page) via `core-splashscreen`.
- **Release signing complete** (#3) — keystore `release.jks` (alias `asmaulhusna`) created; `signingConfigs.release` reads git-ignored `keystore.properties`. `bundleRelease` builds a signed, verified AAB. Keystore + password live only on the dev machine (must be backed up).
- **Privacy Policy published** (#4) — hosted at https://sites.google.com/view/asma-al-husna/home (source: `docs/PRIVACY_POLICY.md`). Live, no placeholders, contact `emdadev@gmail.com`, content verified to match app behaviour. _Still: paste the URL into Play Console (Store listing + Data Safety)._
- **R8 shrinking enabled** (#17) — `isMinifyEnabled` + `isShrinkResources` on for release, wired to `proguard-rules.pro`. `assembleRelease` verified: 22M → 4.7M, mapping emitted, audio + FCM service preserved.
- **Release lint fix** — `lintVitalRelease` (which only runs on release builds) rejected the backup rules: with an `<include>` allowlist, the Firebase `<exclude>` entries pointed at non-included paths and errored the build. Removed the redundant excludes — the allowlist already omits those files. (Debug builds never caught this.)
- **Authenticity disclaimer** (#8, mitigated) — per-name detail screens show a prominent note that the Virtue/Practice text is traditional and varies in authenticity, pointing to Virtues → "On Authenticity". Localized in all 7 languages (`authenticity_note`). A scholar review of the per-name content is still recommended.

---

## 🔴 Blockers (will get rejected / can't ship)

| # | Category | Gap | Fix |
|---|---|---|---|
| 1 | ✅ **Resolved** | **Third-party audio without license** — was hotlinking MP3s from `MohammedAbidNafi/99-Names-of-Allah` (no LICENSE). | **Done:** 99 CC BY-SA 4.0 clips (Mohammed Sadiq / Wikimedia Commons) bundled in `res/raw/`, played offline; attribution shown in Settings › About. Keep the CC BY-SA credit visible and the audio under CC BY-SA while these files ship. |
| 2 | ✅ **Resolved** | **FCM token upload to a placeholder server** — `TokenUploader` POSTed the device push token to `https://api.example.com/v1/devices`. | **Done:** `TokenUploader` removed (no backend). FCM receive is retained for broadcast announcements; target from the Firebase console or topics — no token is uploaded, so no identifier leaves the device. |
| 3 | ✅ **Resolved** | **No signed release AAB** — `buildTypes.release` had no `signingConfig`; no keystore. | **Done:** keystore `release.jks` (alias `asmaulhusna`, RSA 2048, valid to 2053) created; creds in git-ignored `keystore.properties`. `./gradlew bundleRelease` produces a **signed, verified** `app/build/outputs/bundle/release/app-release.aab` (6.3 MB, R8-shrunk). ⚠️ **Back up `release.jks` + password off-machine — losing them means you can never update the app.** |
| 4 | ✅ **Resolved** | **No Privacy Policy URL** — required because the app declares `INTERNET` and integrates FCM. | **Done:** hosted at **https://sites.google.com/view/asma-al-husna/home** — live, no placeholders, contact `emdadev@gmail.com`. Verified content matches actual behaviour (offline audio, no token upload, prefs back up to the user's own Google account). **You still paste this URL into Play Console → Store listing and Data Safety.** |
| 5 | **Console** | **Data Safety form not prepared** — declare any backup of preferences and Firebase SDK behaviour. _(The push token is no longer collected or sent anywhere; audio makes no network calls.)_ | Complete the Data Safety questionnaire honestly. |
| 6 | **Console** | **Content rating not obtained** — IARC questionnaire required. | Complete in Play Console. |

## 🟡 High-risk (may be rejected or flagged)

| # | Category | Gap | Fix |
|---|---|---|---|
| 7 | ✅ **Resolved** | **Exact-alarm permission was a restricted/policy risk** — exact alarms are reserved by Play for apps whose core function is alarms/reminders; this is a reference app. | **Done:** dropped both `USE_EXACT_ALARM` and `SCHEDULE_EXACT_ALARM`; `ReminderScheduler` now always uses inexact `setAndAllowWhileIdle` (doze-friendly, arrives within the OS window). No restricted permission, no Console alarm declaration needed. |
| 8 | 🟡 **Mitigated** | **Fazilat/amal authenticity** — per-name virtue/practice text may include weak/unsourced narrations. | **Done (disclaimer):** each name's detail screen now shows a prominent authenticity note under the Virtue/Practice sections, marking them as traditional teachings of varying authenticity and pointing to Virtues → "On Authenticity" — localized in all 7 languages (`authenticity_note`). **Still recommended:** a qualified scholar review of the per-name content itself. |
| 9 | **Config** | **`google-services.json` is gitignored** but the app links Firebase. | Fine for local builds; ensure the release/CI build has the correct file for the production Firebase project (or remove Firebase if unused). |
| 10 | ✅ **Resolved** | **No offline fallback for audio** — used to stream and only show a toast on failure. | **Done:** audio is bundled in `res/raw/` and plays offline; no network path to fail. |
| 11 | ✅ **Resolved** | **`allowBackup=true` with default (empty) backup rules** — prefs auto-backed up to Google. | **Done:** `backup_rules.xml` (legacy) + `data_extraction_rules.xml` (12+) now allowlist only the user's prefs and exclude Firebase id files. Verified the `<include>` paths match the real `SharedPreferences` files (`favorites`, `zikir`, `settings`, `reminders`) — so backup actually captures them. **Data Safety:** declare that app preferences may be backed up to the user's own Google account. |
| 12 | **Ops** | **No crash reporting** — not required, but appeals/debugging are hard without it. | Add Crashlytics (Firebase is already integrated). |

## 🟢 Minor polish (won't block, but affects quality/ratings)

| # | Category | Gap | Fix |
|---|---|---|---|
| 13 | UX | No splash screen | Adopt the Android 12+ SplashScreen API (`core-splashscreen`). |
| 14 | Console | No app category set | Books & Reference or Lifestyle. |
| 15 | Code | Remaining deprecated API | `Icons.Filled.VolumeUp` → `Icons.AutoMirrored.Filled.VolumeUp` in `DetailScreen`. |
| 16 | Assets | No store listing assets | 2–8 phone screenshots + 1024×500 feature graphic (optional tablet shots). |
| 17 | ✅ **Resolved** | R8 shrinking disabled | **Done:** `isMinifyEnabled` + `isShrinkResources` on for release, wired to `proguard-rules.pro`. Verified with `assembleRelease`: **22M → 4.7M**, mapping file emitted, all 99 audio clips kept & reachable, FCM service retained. _Runtime smoke-test on a device still recommended before shipping._ |
| 18 | Quality | No meaningful tests | Only the stub instrumented test; add unit tests for `FavoritesStore`, `ZikirStore`, and content mapping. |
| 19 | i18n | Hard-coded Bangla toasts | `AudioPlayer` toasts are literal Bangla strings — extract to resources. |
| 20 | Branding | Generic name / package | "Asma al-Husna" and `com.islamic.asmaulhusna` are common; the package is immutable once published. Consider a distinctive package (e.g. an owned domain). |
| 21 | Build | Version strategy | `versionCode=1` is fine for first submit; plan a bump-on-release scheme. |
| 22 | Data | Small `AudioPlayer` bug | id 67 references `ahad.mp4` (wrong extension) among `.mp3` files. |

_Note: an in-app dark-mode toggle is intentionally **not** offered — the app commits to a single dark "Mushaf" theme by design, so that earlier item is dropped rather than a gap._

---

## 📋 Suggested publish order

> Full step-by-step publishing + upload walkthrough: **[PUBLISHING.md](PUBLISHING.md)**.

1. ~~**Audio**: bundle licensed audio~~ ✅ done — 99 CC BY-SA 4.0 clips bundled offline in `res/raw/` with in-app attribution — #1, #10, #22
2. ~~**FCM**: remove `TokenUploader`~~ ✅ done — `TokenUploader` deleted (no backend); FCM receive kept, no token uploaded — #2, #9, #12
3. ~~**Exact alarms**: decide `USE_EXACT_ALARM` vs inexact~~ ✅ done — dropped exact-alarm perms, using inexact `setAndAllowWhileIdle` — #7
4. ~~**Backup**: fill in backup rules~~ ✅ done — allowlisted user prefs, verified filenames match, Firebase ids excluded — #11
5. ~~**Privacy Policy**: write + host, get the URL~~ ✅ done — https://sites.google.com/view/asma-al-husna/home — #4
6. ~~**Release build**: R8 + keystore~~ ✅ done — R8 enabled (#17), keystore created, `signingConfigs.release` wired (#3)
7. ~~**Build signed AAB**: `./gradlew bundleRelease`~~ ✅ done — signed & verified `app-release.aab` (6.3 MB). Upload this to Play Console. **Back up the keystore + password.**
8. **Play Console** ($25 one-time): Data Safety, Content Rating, Target Audience, store assets — #5, #6, #14, #16
9. **Rollout**: Internal → Closed → Production tracks
