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
- **Audio blocker mitigated** — unlicensed streaming disabled behind `AudioPlayer.ENABLED = false` (button hidden).
- **FCM token upload neutralized** (#2) — guarded behind `TokenUploader.BACKEND_CONFIGURED = false`; no identifier leaves the device until a real endpoint is set.
- **Exact-alarm permission** (#7) — switched to `USE_EXACT_ALARM` (auto-granted for alarm/reminder apps) on 33+, `SCHEDULE_EXACT_ALARM` capped at `maxSdkVersion=32`; exact Suhoor/Iftar timing preserved. _(Play Console still asks to confirm the alarm use case.)_
- **`VolumeUp` deprecation** (#15) — switched to `Icons.AutoMirrored.Filled.VolumeUp`.
- **Hard-coded Bangla toasts** (#19) — `AudioPlayer` toasts extracted to localized string resources.
- **`ahad.mp4` typo** (#22) — corrected to `.mp3`.

---

## 🔴 Blockers (will get rejected / can't ship)

| # | Category | Gap | Fix |
|---|---|---|---|
| 1 | **Legal** | **Third-party audio without license** — `AudioPlayer` hotlinks MP3s from `MohammedAbidNafi/99-Names-of-Allah` (no LICENSE). **Interim mitigation applied: audio feature disabled (`AudioPlayer.ENABLED = false`), so nothing streams.** | Still to do before re-enabling: bundle your own/commissioned or verified CC0/CC-BY audio in `assets/` (don't hotlink; self-hosting the *same* files does **not** cure copyright), then flip the flag. |
| 2 | **Legal/Privacy** | **FCM token upload to a placeholder server** — `TokenUploader` POSTs the device push token to `https://api.example.com/v1/devices`. | Either remove `TokenUploader`/FCM until there's a real backend, or point it at a secured HTTPS endpoint you own and declare it in Data Safety. Shipping token collection to `example.com` is broken and a policy risk. |
| 3 | **Build** | **No signed release AAB** — `buildTypes.release` has no `signingConfig`; no keystore. | Create a keystore, add `signingConfigs.release` (creds via `keystore.properties`, never committed), build `bundleRelease`. |
| 4 | **Legal** | **No Privacy Policy URL** — required: app declares `INTERNET`, streams audio, and integrates FCM (push token is a device identifier). | Publish a privacy policy (GitHub Pages is fine) and add the URL in Play Console. |
| 5 | **Console** | **Data Safety form not prepared** — must declare audio streaming network use, FCM push token (an identifier), and any backup of preferences. | Complete the Data Safety questionnaire honestly once #2 is resolved. |
| 6 | **Console** | **Content rating not obtained** — IARC questionnaire required. | Complete in Play Console. |

## 🟡 High-risk (may be rejected or flagged)

| # | Category | Gap | Fix |
|---|---|---|---|
| 7 | **Policy** | **`SCHEDULE_EXACT_ALARM` is a restricted permission** — apps targeting 33+ that request it must have alarm/reminder as a core feature and justify it; otherwise Play flags it. | Justify it in the Console declaration, switch to `USE_EXACT_ALARM` (allowed for alarm/reminder apps), or drop to inexact alarms (the scheduler already falls back). |
| 8 | **Content** | **Fazilat/amal authenticity** — per-name virtue/practice text may include weak/unsourced narrations. (A Virtues screen with hadith references + an authenticity note now exists — good, but per-name text still needs review.) | Cite sources or mark as "traditional"; keep the disclaimer prominent. |
| 9 | **Config** | **`google-services.json` is gitignored** but the app links Firebase. | Fine for local builds; ensure the release/CI build has the correct file for the production Firebase project (or remove Firebase if unused). |
| 10 | **UX** | **No offline fallback for audio** — network failure only shows a toast (and the toast is hard-coded Bangla). | Bundle/caching once audio is licensed; localize the toasts. |
| 11 | **Config** | **`allowBackup=true` with default (empty) backup rules** — prefs auto-back up to Google. | Fill in `backup_rules.xml` / `data_extraction_rules.xml` (or set `allowBackup=false`) and reflect it in Data Safety. |
| 12 | **Ops** | **No crash reporting** — not required, but appeals/debugging are hard without it. | Add Crashlytics (Firebase is already integrated). |

## 🟢 Minor polish (won't block, but affects quality/ratings)

| # | Category | Gap | Fix |
|---|---|---|---|
| 13 | UX | No splash screen | Adopt the Android 12+ SplashScreen API (`core-splashscreen`). |
| 14 | Console | No app category set | Books & Reference or Lifestyle. |
| 15 | Code | Remaining deprecated API | `Icons.Filled.VolumeUp` → `Icons.AutoMirrored.Filled.VolumeUp` in `DetailScreen`. |
| 16 | Assets | No store listing assets | 2–8 phone screenshots + 1024×500 feature graphic (optional tablet shots). |
| 17 | Build | R8 shrinking disabled | `release { optimization.enable = false }` — enable for a smaller AAB + obfuscation. |
| 18 | Quality | No meaningful tests | Only the stub instrumented test; add unit tests for `FavoritesStore`, `ZikirStore`, and content mapping. |
| 19 | i18n | Hard-coded Bangla toasts | `AudioPlayer` toasts are literal Bangla strings — extract to resources. |
| 20 | Branding | Generic name / package | "Asma al-Husna" and `com.islamic.asmaulhusna` are common; the package is immutable once published. Consider a distinctive package (e.g. an owned domain). |
| 21 | Build | Version strategy | `versionCode=1` is fine for first submit; plan a bump-on-release scheme. |
| 22 | Data | Small `AudioPlayer` bug | id 67 references `ahad.mp4` (wrong extension) among `.mp3` files. |

_Note: an in-app dark-mode toggle is intentionally **not** offered — the app commits to a single dark "Mushaf" theme by design, so that earlier item is dropped rather than a gap._

---

## 📋 Suggested publish order
1. **Audio**: bundle licensed audio in `assets/` (or disable the feature) — #1, #10, #22
2. **FCM**: remove `TokenUploader`/Firebase if there's no backend, else wire a real secured endpoint — #2, #9, #12
3. **Exact alarms**: decide `USE_EXACT_ALARM` vs inexact and update the manifest/declaration — #7
4. **Backup**: fill in backup rules or set `allowBackup=false` — #11
5. **Privacy Policy**: write + host, get the URL — #4
6. **Release build**: keystore + `signingConfigs.release`, enable R8 — #3, #17
7. **Build signed AAB**: `./gradlew bundleRelease`
8. **Play Console** ($25 one-time): Data Safety, Content Rating, Target Audience, store assets — #5, #6, #14, #16
9. **Rollout**: Internal → Closed → Production tracks
