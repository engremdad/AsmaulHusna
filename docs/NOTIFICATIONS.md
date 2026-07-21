# Notifications — Local Reminders & Firebase Cloud Messaging

The app uses a **hybrid** notification strategy:

| Job | Mechanism | Why |
|---|---|---|
| Suhoor / Iftar / daily-name reminders (fire at an exact local time, offline) | **On-device `AlarmManager`** | No delivery-time guarantee, network, or per-user server data needed. A missed Iftar ping is a bad experience. |
| Ramadan greetings, announcements, engagement nudges (same message to many, sent on demand) | **Firebase Cloud Messaging (FCM)** | Exactly what FCM is for — broadcast from the Firebase console or a backend. |

Local reminders work out of the box. FCM stays **dormant until you add `google-services.json`** (see setup below).

---

## Architecture

All notification code lives in `app/src/main/java/com/islamic/asmaulhusna/notify/`:

| File | Role |
|---|---|
| `NotificationHelper.kt` | Creates the two channels (`reminders`, `announcements`) and posts notifications. Deep-links via the `open_name_id` extra. |
| `Reminders.kt` | `ReminderType` enum (Daily Name / Suhoor / Iftar) with stable IDs + `ReminderPrefs` (SharedPreferences toggle + time per reminder). |
| `ReminderScheduler.kt` | Schedules each reminder as a daily `AlarmManager` alarm. Prefers exact alarms; falls back to inexact (doze-friendly) when exact-alarm access isn't granted. |
| `ReminderReceiver.kt` | Fires on the alarm: posts the notification, then re-arms for the next day. |
| `BootReceiver.kt` | Re-arms every enabled reminder after a device reboot (alarms are cleared on boot). |
| `AsmaulHusnaMessagingService.kt` | `FirebaseMessagingService` — handles `onNewToken` and `onMessageReceived` (FCM). |

UI: `ui/NotificationSettingsScreen.kt` — reached from the bell icon in the Home top bar. Per-reminder switch + tap-to-set time.

`MainActivity` creates channels, reschedules alarms on launch, requests the `POST_NOTIFICATIONS` permission, and routes tapped notifications to the right name's detail screen.

### Permissions (already in `AndroidManifest.xml`)
- `POST_NOTIFICATIONS` — runtime-requested on Android 13+ (requested when a reminder is enabled).
- `SCHEDULE_EXACT_ALARM` — for on-time Suhoor/Iftar; degrades gracefully to inexact if the user/OS revokes it.
- `RECEIVE_BOOT_COMPLETED` — to re-arm alarms after reboot.

---

## Enabling Firebase Cloud Messaging

FCM activation is gated by the presence of `app/google-services.json`. Until that
file exists, `app/build.gradle.kts` skips applying the `google-services` plugin, so
the app builds and runs normally with local reminders only.

### 1. Create the Firebase project
1. Go to <https://console.firebase.google.com> → **Add project**.
2. In the project, **Add app → Android**.
3. Android package name: **`com.islamic.asmaulhusna`** (must match exactly).
4. (Optional) add a debug SHA-1 if you later use other Firebase auth features — not needed for FCM.

### 2. Add the config file
1. Download **`google-services.json`**.
2. Place it at **`app/google-services.json`** (same folder as `app/build.gradle.kts`).
3. Rebuild. The `google-services` plugin now applies automatically and FCM initializes on next launch.

> **Do not commit** `google-services.json` to a public repo if it isn't already ignored — treat it as project config. (It contains project identifiers, not secret keys, but keep it out of public history by preference.)

### 3. Get the device token
On launch, the registration token is logged:
```
adb logcat -s FCM
# → FCM registration token: <token>
```
Send this token to your backend / marketing tool to target the device, or use **topics** (e.g. subscribe all users to a `ramadan` topic) for broadcasts.

### 4. Send a test / scheduled message
**Firebase Console → Messaging → New campaign → Notifications:**
- Compose title + body (Bengali is fine).
- Target: your app, or a topic.
- **Scheduling:** send now, or schedule around Suhoor/Iftar for cultural relevance.
- Custom deep-link: add a data key **`name_id`** with a value `1`–`99` to open that name's detail when tapped.

Supported message payloads (`onMessageReceived`):
| Source | Keys used |
|---|---|
| Notification payload | `title`, `body` |
| Data payload (fully custom) | `title`, `body`, `name_id` (optional) |

FCM messages post to the **`announcements`** channel (see `default_notification_channel_id` meta-data in the manifest).

---

## Testing local reminders without waiting for the clock

1. Open the app → bell icon → enable a reminder and grant notification permission.
2. Set its time to **1–2 minutes** ahead, then leave the app.
3. The notification should fire; tapping the **Daily Name** one opens that name's detail.

To verify reboot re-arming: enable a reminder, reboot the device/emulator, confirm the alarm is re-scheduled (it fires at its next time without reopening the app).

### Exact-alarm note
On Android 12+ the OS may not grant exact alarms automatically. Without it, reminders still arrive but may be delayed by a few minutes under Doze. If precise timing matters, guide users to **Settings → Apps → Asmaul Husna → Alarms & reminders → Allow**.

---

## Known limitations / follow-ups
- **Suhoor/Iftar times are user-set**, not auto-calculated from location. A future enhancement is real prayer-time computation (latitude/longitude + calculation method) to set them automatically.
- FCM `onNewToken` currently only logs the token — wire it to your backend to enable per-device/topic targeting.
- The status-bar icon is `res/drawable/ic_stat_name.xml` (monochrome crescent + star); replace if you design a dedicated notification glyph.
