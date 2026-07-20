# Google Play Store — Publishing Gap Analysis

Status legend: 🔴 Blocker · 🟡 High-risk · 🟢 Minor polish

## 🔴 Blockers (will get rejected)

| # | Category | Gap | Details / Fix |
|---|---|---|---|
| 1 | Legal | Third-party audio without license | Streaming from `MohammedAbidNafi/99-Names-of-Allah` — no LICENSE file. Copyright/DMCA risk. Self-host or use CC0/CC-BY audio with attribution. |
| 2 | Build | No signed release AAB | Play Console requires `.aab`. Need keystore + `signingConfigs.release` + `bundleRelease`. |
| 3 | Legal | No Privacy Policy URL | Required because `INTERNET` permission is declared. Host on GitHub Pages. |
| 4 | Assets | Default Android launcher icon | `mipmap/ic_launcher` is still the green robot. Design adaptive icon (foreground + background). |
| 5 | Console | Data Safety form not prepared | Must declare network activity (audio streaming) and any data collection. |
| 6 | Console | Content rating not obtained | Complete IARC questionnaire in Play Console. |

## 🟡 High-risk (may get rejected or flagged)

| # | Category | Gap | Details / Fix |
|---|---|---|---|
| 7 | Content | Fazilat/amal without sources | Many statements come from weak/mawdu hadith. Add disclaimer + cite Bukhari/Muslim/Tirmidhi; mark others as "traditional". |
| 8 | Branding | Generic app name | "আসমাউল হুসনা" used by 100s of apps. Add distinctive suffix to avoid duplication check. |
| 9 | UX | No offline fallback for audio | On network failure only a toast. Cache played files in `cacheDir` or bundle a small set. |
| 10 | Config | `allowBackup=true` with no rules | Prefs auto-back up to Google. Declare in Data Safety or scope backup. |
| 11 | Ops | No crash reporting | Not required but rejection appeals are hard without Crashlytics/Firebase logs. |

## 🟢 Minor polish (won't block, but hurts ratings)

| # | Category | Gap | Details / Fix |
|---|---|---|---|
| 12 | UX | No splash screen | Adopt Android 12+ SplashScreen API. |
| 13 | Console | No app category set | Books & Reference or Lifestyle in Manifest + Console. |
| 14 | Code | Deprecated Compose APIs | `Icons.Filled.VolumeUp` and `centerAlignedTopAppBarColors` are deprecated. Switch to AutoMirrored / `topAppBarColors`. |
| 15 | Assets | No store screenshots or feature graphic | Need 2–8 phone screenshots + 1024×500 feature graphic. Optional 7" and 10" tablet screenshots. |
| 16 | UX | No in-app dark mode toggle | Currently follows system. Add manual toggle for user preference. |
| 17 | UX | No share button for names | Common in similar apps. Simple `ACTION_SEND` intent. |
| 18 | UX | Empty-state polish | Favorites has minimal placeholder — good. Add nice empty state for search. |
| 19 | Build | Version code strategy | `versionCode=1` / `versionName=1.0`. Fine for first submit; plan bump-on-release strategy. |
| 20 | Build | R8 shrinking disabled for release | Currently `optimization.enable=false`. Enable for smaller AAB and obfuscation. |
| 21 | Quality | No meaningful tests | Only stub `ExampleInstrumentedTest`. Add unit tests for FavoritesStore and data mapping. |
| 22 | i18n | Bengali-only strings | No `values-en/strings.xml`. Hard-coded Bangla in composables. Extract to string resources + add English. |
| 23 | Branding | Generic package name | `com.islamic.asmaulhusna` is generic and immutable once published. Consider owning a domain (`com.yourname.asmaulhusna`). |

## 📋 Suggested publish order

1. Replace audio source (self-host with license) — #1
2. Design adaptive launcher icon — #4
3. Write Privacy Policy + host on GitHub Pages — #3
4. Add disclaimer screen for fazilat authenticity — #7
5. Extract strings to resources + add English — #22
6. Enable R8 minification in release — #20
7. Generate signing keystore + configure signing (never commit `.jks`) — #2
8. Build signed AAB — `./gradlew bundleRelease`
9. Create Play Console app ($25 one-time) → Data Safety, Content Rating, Target Audience, screenshots — #5, #6, #15
10. Internal → Closed → Production testing tracks
