# Privacy Policy — Asma al-Husna

_Last updated: 2026-07-22_

Asma al-Husna ("the app") is an offline reference app for the 99 Names of Allah.
This policy explains what the app does and does not do with your data.

> **Draft.** Review, fill in the **[CONTACT EMAIL]** and effective date, then host this
> (e.g. GitHub Pages) and paste the public URL into Play Console → Store listing and
> Data Safety. Keep it in sync with the app's actual behavior.

## Summary
The app does **not** require an account, and does **not** collect, sell, or share
personal information. Your data (favorites, counts, settings) stays on your device.

## Information the app stores on your device
The following are saved only in the app's local storage (Android SharedPreferences) and
never sent to us:
- **Favorites** — names you mark as favorite.
- **Zikir counts and targets** — per-name tap counts and goals.
- **Settings** — chosen language, display options, and reminder times.

If you have Android Backup enabled, these preferences may be backed up to **your own**
Google account (Google Drive), governed by Google's privacy policy — not by us.

## Permissions the app requests
- **Notifications (POST_NOTIFICATIONS)** — to show the daily name, Suhoor, and Iftar
  reminders you enable. You control these in Settings and in system settings.
- **Exact alarms (USE_EXACT_ALARM / SCHEDULE_EXACT_ALARM)** — so time-critical reminders
  (e.g. Suhoor) fire at the correct minute.
- **Run at startup (RECEIVE_BOOT_COMPLETED)** — to re-arm your reminders after the device
  restarts.
- **Internet (INTERNET)** — used only if optional online features are enabled (see below).

## Network and third-party services
- **Firebase Cloud Messaging (FCM):** the app includes Google's FCM library so it can
  receive optional announcement notifications. FCM may assign a device registration token,
  which is processed by Google. **The app does not upload this token to any server of ours**
  (there is no backend; announcements are targeted from the Firebase console or by topic).
  See Google's privacy policy for FCM.
- **Audio:** the pronunciation recitation clips are **bundled inside the app and play fully
  offline** — nothing is downloaded or streamed when you tap Listen. (Recordings by
  Mohammed Sadiq via Wikimedia Commons, licensed CC BY-SA 4.0; credited in Settings › About.)
- The app shows **no ads** and uses **no analytics or advertising identifiers**.

## Children's privacy
The app is suitable for all ages and does not knowingly collect personal information from
children.

## Data deletion
Because data is stored only on your device, you can remove all of it by clearing the app's
storage (Settings → Apps → Asma al-Husna → Storage → Clear data) or uninstalling the app.

## Changes to this policy
We may update this policy as the app changes. Material changes will be reflected here with a
new "Last updated" date.

## Contact
Questions about this policy: **[CONTACT EMAIL]**
