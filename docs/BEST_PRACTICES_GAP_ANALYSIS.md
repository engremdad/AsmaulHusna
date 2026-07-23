# Engineering Best-Practices Gap Analysis

_App: Asma al-Husna · `com.islamic.asmaulhusna` · Kotlin + Jetpack Compose · single-activity_
_Last reviewed: 2026-07-23 · AGP 9.3.0 · Kotlin 2.2.10 · compileSdk 36 · minSdk 24 · targetSdk 36_

> Scope: **engineering** quality — architecture, state, testing, tooling, build, performance.
> Store **policy/publishing** readiness is tracked separately in
> [PLAY_STORE_GAP_ANALYSIS.md](PLAY_STORE_GAP_ANALYSIS.md); crash-reporting (#12 there) and
> test coverage (#18 there) overlap and are expanded here.

Status legend: 🔴 High-value · 🟡 Medium · 🟢 Low / nice-to-have · ✅ Already solid

---

## ✅ What's already solid (keep it)

- **Single dark "Mushaf" theme, edge-to-edge, splash API** — deliberate, consistent design.
- **Localization** — 7 languages, English base `values/`, RTL support, `AutoMirrored` icons.
- **Accessibility basics** — icons pass `contentDescription` positionally: meaningful icons get
  string resources (`cd_back`, `cd_favorites`, …), decorative ones correctly get `null`
  (`HomeScreen.kt:78`, `HomeScreen.kt:140`). Good discipline — verify with a TalkBack pass (🟢 below).
- **Secrets hygiene** — `keystore.properties`, `*.jks`, `*.keystore`, `google-services.json`,
  `local.properties` all git-ignored (`.gitignore:16-22`). Signing reads from a properties file.
- **R8** — code + resource shrinking on for release with app-specific keep rules.
- **Build performance** — Gradle configuration cache enabled, version catalog (`libs.versions.toml`)
  is the single source for dependency versions.
- **Permission model** — inexact doze-friendly alarms (no restricted exact-alarm), receivers
  correctly scoped (`ReminderReceiver` not exported, `BootReceiver` exported only for BOOT_COMPLETED).

---

## 🔴 High-value gaps

### 1. No CI/CD pipeline
There is no `.github/workflows/`. Nothing builds, tests, or lints on push/PR, so regressions
(compile breaks, failing tests, new lint errors) are only caught locally and by luck.

**Fix:** add a GitHub Actions workflow running on push/PR:
`./gradlew assembleDebug testDebugUnitTest lintDebug`. Cache Gradle. Later gate the release
build (`bundleRelease`) behind it. This is the single highest-leverage improvement — it makes
every other item below self-enforcing.

### 2. Test coverage is stub-only
`ExampleUnitTest` (`assertEquals(4, 2+2)`) and `ExampleInstrumentedTest` are the only tests —
zero real coverage. Yet several pieces are pure and trivially testable:
- `ReminderScheduler.nextTriggerMillis(hour, minute)` — date/rollover logic (`ReminderScheduler.kt:49`),
  currently only refactorable-by-hope. Extract it to a pure function and unit-test the "already
  passed today → tomorrow" boundary.
- `FavoritesStore.toggle` / `isFavorite` — add/remove idempotency (`FavoritesStore.kt`).
- `ZikirStore` — `increment`, `reset`, `setTarget(0)` clears (`ZikirStore.kt`) — use Robolectric
  or a `SharedPreferences` fake.
- Content mapping in `AsmaulHusna.kt` — assert exactly 99 entries, ids `1..99` contiguous, and
  that `AudioPlayer.RES` has one raw resource per id (guards the historical `.mp4` typo class of bug).

**Fix:** start with the pure logic (no Android deps), then the stores via Robolectric.

### 3. Data layer depends on the Compose runtime
`FavoritesStore` and `ZikirStore` (`data/`) import `androidx.compose.runtime.mutableStateOf` and
expose Compose `State` directly (`FavoritesStore.kt:4,8`, `ZikirStore.kt:4`). This couples the
data layer to the UI toolkit: the stores can only be observed from a composition, can't be unit-
tested without the Compose runtime, and can't be reused off the main thread.

**Fix:** keep the data layer Compose-free — expose `StateFlow`/`Flow` (or plain suspend reads),
and let a `ViewModel` bridge to Compose via `collectAsStateWithLifecycle()`. This also unlocks
items 2 and 4.

---

## 🟡 Medium gaps

### 4. No ViewModel / unidirectional-data-flow layer
State objects are constructed inside composition (`remember { FavoritesStore(context) }`,
`MainActivity.kt:68-69`). `remember` survives recomposition but **not** Activity recreation
(rotation, process death) — the stores are rebuilt and reload from prefs each time. It works here
only because everything is prefs-backed; any in-memory/in-flight state would be lost. It also
threads dependencies manually through every screen's parameter list.

**Fix:** introduce `ViewModel`(s) (`androidx.lifecycle:lifecycle-viewmodel-compose`) as the
state holders, obtained via `viewModel()`. Screens take state + event lambdas, not store objects.

### 5. SharedPreferences with main-thread reads; consider DataStore
- `ZikirStore.load()` iterates `prefs.all` at construction (`ZikirStore.kt:20-26`) — a synchronous
  disk read on the main thread. Small today, but it runs on every store creation.
- Jetpack **DataStore** is the current recommendation over `SharedPreferences` (async, transactional,
  Flow-based, no accidental main-thread I/O). Migrating the four prefs files (favorites, zikir,
  settings, reminders) pairs naturally with items 3–4.

**Fix (incremental):** at minimum move reads off the main thread; strategically, migrate to
Preferences DataStore.

### 6. `MediaPlayer.create()` runs synchronous prepare on the main thread
`AudioPlayer.play()` calls `MediaPlayer.create(...)` (`AudioPlayer.kt:65`), which synchronously
prepares the clip on the calling (main) thread. For short bundled clips this is usually fine, but
it's an ANR risk pattern and blocks the UI during decode.

**Fix:** use `MediaPlayer().apply { setDataSource(...); setOnPreparedListener { start() }; prepareAsync() }`,
or prepare on a background dispatcher. `AudioAttributes` (USAGE_MEDIA) should also be set so the
clip respects the media volume/focus rules.

### 7. No static analysis / formatting tooling
No detekt, ktlint, or Spotless, and no Android `lint { }` block in `app/build.gradle.kts`. Lint
only runs at its defaults, and `lintVital` fires solely on release builds (as the Play doc notes,
that let a backup-rules error slip past debug builds).

**Fix:** add a `lint { warningsAsErrors = true; checkDependencies = true; baseline = file("lint-baseline.xml") }`
block, and adopt **detekt** (or ktlint via Spotless) with a config committed to the repo. Wire both
into the CI job from item 1.

### 8. No dependency-injection framework
Dependencies are wired by hand through composables. Acceptable at this size, but as the store/
ViewModel/DataStore layers grow it becomes boilerplate and complicates testing (no seam to inject
fakes).

**Fix:** adopt **Hilt** when item 4 lands — `@HiltViewModel`, constructor-injected stores, test
modules for fakes. Low urgency; revisit after ViewModels exist.

---

## 🟢 Low / nice-to-have

### 9. Java/JVM target is 11
`compileOptions` pins `sourceCompatibility`/`targetCompatibility` to `JavaVersion.VERSION_11`
(`app/build.gradle.kts:66-67`), and there's no explicit Kotlin `jvmTarget`. On AGP 9 / a modern
toolchain, **17** is the current baseline and unlocks newer language/library features.
**Fix:** bump both to 17 and set `kotlin { jvmToolchain(17) }` (or `compilerOptions.jvmTarget`).

### 10. No release versioning strategy
`versionCode = 1` / `versionName = "1.0"` are hard-coded (`app/build.gradle.kts:24-25`). Fine for
the first submission, but every Play update needs a unique, monotonically increasing `versionCode`.
**Fix:** derive `versionCode` from CI run number or git commit count, and adopt a semver scheme
for `versionName`.

### 11. Ad-hoc logging, no crash reporting
Errors go to `Log.e` with per-file string tags (`AudioPlayer.kt:73,81`) and no central strategy;
production crashes are invisible (also Play doc #12). Firebase is already integrated.
**Fix:** add Firebase **Crashlytics** (drop-in given the existing Firebase BoM) and, optionally,
a thin logging wrapper so tags/levels are consistent and strippable in release.

### 12. TalkBack / accessibility audit
`contentDescription` discipline is good (see ✅), but there's no evidence of a screen-reader pass,
touch-target sizing check (≥48dp), or dynamic-type stress test — and the app already offers an
in-app text-scale multiplier on top of the system font scale, which is worth verifying at extremes.
**Fix:** run one TalkBack + large-font pass over each screen and note results.

---

## 📋 Suggested order

1. **CI (item 1)** — do this first; it enforces everything after it.
2. **Unit tests for pure logic (item 2)** — `nextTriggerMillis`, content-count invariants, stores.
3. **Decouple data layer from Compose + ViewModels (items 3, 4)** — the core architectural refactor;
   unlocks clean testing and DataStore.
4. **Tooling (item 7)** — detekt + lint gating, wired into CI.
5. **DataStore + off-main audio (items 5, 6)** — correctness/perf polish.
6. **Crashlytics, JVM 17, versioning, a11y pass (items 9–12)** — incremental hardening.
7. **Hilt (item 8)** — once ViewModels exist and manual wiring hurts.

_Nothing here blocks shipping — the app is publishable per the Play Store gap analysis. These are
maintainability, testability, and robustness improvements that lower the cost of the next feature._
