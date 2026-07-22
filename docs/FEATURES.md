# Asma al-Husna — Feature List

> The 99 Names of Allah — an illuminated "Mushaf" reading app.
> Package `com.islamic.asmaulhusna` · version 1.0 (versionCode 1) · minSdk 24 · targetSdk 36

_Last updated: 2026-07-22 (audio enabled — bundled, licensed offline pronunciation clips)_

This document is the running catalogue of what the app does. **It is updated whenever a
task adds, changes, or removes a feature** — see [Maintenance](#maintenance) at the bottom.

---

## Browsing the Names
- **99 Names grid** — all names in a 3-column grid, each cell showing the number, Arabic
  script, and the localized name.
- **Name of the Day** — a highlighted "Today" card that surfaces one name each day.
- **Search** — filter names live by transliteration, localized name, meaning, or Arabic text.

## Name detail
- **Name hero** — large Arabic calligraphy with transliteration and localized name.
- **Meaning / Virtue / Practice** sections for every name.
- **Listen** — plays the name's recitation from a bundled MP3 (fully offline, no network).
  The 99 clips are human pronunciation recordings by Mohammed Sadiq via Wikimedia Commons,
  licensed CC BY-SA 4.0; attribution is shown in **Settings › About › Audio credits**.
  Gated by `AudioPlayer.ENABLED` (now `true`).
- **Favorite** — toggle a name as favorite from the detail top bar.
- **Share** — send the full name text (Arabic, transliteration, name, Meaning, Virtue,
  Practice) with an "— Asma al-Husna" footer via the system share sheet.
- **Copy** — copy the same full text to the clipboard (toast confirmation on Android ≤12;
  Android 13+ shows its own system confirmation).

## Zikir counter (per name)
- **Tap counter** — large circular tap target on the detail screen to count recitations,
  with haptic feedback on each tap.
- **Target** — set a goal (presets 33/99/100/500/1000 or a custom value) with a progress
  bar and a "target reached" state.
- **Reset** — clear a name's count.
- **Count sound** — optional tick sound on each tap, toggled in Settings (off by default).
- Counts and targets persist per name across launches.

## Virtues of the Names (Fadā'il)
- A dedicated reading screen on the virtues of learning the 99 names, with Hadith
  references (Bukhari, Muslim, Tirmidhi, Abu Dawud, Ibn Majah).
- Reached from a titled **"Virtues of the Names"** banner on the Home screen.

## Favorites
- A Favorites screen listing all names the user has starred, with an empty state.

## Localization & language
- **7 languages** — English, Arabic, Bangla, Hindi, Indonesian, Turkish, Urdu
  (UI + full 99-names content).
- **RTL support** for Arabic and Urdu.
- **First-run language screen** — on first launch the user picks a language before
  entering the app.
- **In-app language switching** from Settings, applied without restarting the OS locale.

## Settings
- **Language** — change the app language.
- **Display**
  - Show/hide the Zikir counter on detail screens.
  - Zikir count sound toggle.
  - Text size stepper (with live sample).
- **Reminders** — enable/disable and set times for the notification reminders below.
- **About › Audio credits** — dialog with the pronunciation-audio attribution and CC BY-SA
  4.0 licence link (required while the bundled audio ships).

## Notifications
- **Name of the Day** — daily reminder that deep-links to a name's detail screen.
- **Suhoor** and **Iftar** reminders at user-set local times.
- Reminders reschedule after device reboot.
- **Announcements** — remote push (Firebase Cloud Messaging) for Ramadan/special messages.
- **Permission guidance** — if notifications are blocked, Settings shows a warning banner
  with an "Enable" button that opens the system notification settings; it clears
  automatically on return.

## Design
- **Mushaf theme** — a single committed dark palette (deep emerald grounds, gold-leaf
  ornament, warm cream text) with a star-lattice background, evoking a hand-gilded mushaf.
- **Splash screen** — Android 12+ SplashScreen API showing the gold Mushaf mark on the
  emerald page at launch.

---

## Maintenance

Keep this file in sync with the app. **When a task completes that adds, changes, or removes
a user-facing feature, update the relevant section here and bump the "Last updated" date in
the same commit as the code change.** Group entries by the areas above; add a new section if
a feature doesn't fit an existing one.
