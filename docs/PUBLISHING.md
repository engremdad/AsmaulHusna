# Publishing Asma al-Husna to Google Play

_Last updated: 2026-07-22_

A step-by-step guide tailored to this app's actual state. See
[PLAY_STORE_GAP_ANALYSIS.md](PLAY_STORE_GAP_ANALYSIS.md) for the item-by-item readiness
status and [PRIVACY_POLICY.md](PRIVACY_POLICY.md) for the policy source.

## App facts (for the Console forms)
- **Package / applicationId:** `com.islamic.asmaulhusna`
- **Version:** 1.0 (versionCode 1) · minSdk 24 · targetSdk 36
- **Signed bundle to upload:** `app/build/outputs/bundle/release/app-release.aab` (built by `./gradlew bundleRelease`)
- **Privacy policy URL:** https://sites.google.com/view/asma-al-husna/home
- **Upload key:** keystore `release.jks`, alias `asmaulhusna` (RSA 2048, valid to 2053).
  Credentials in git-ignored `keystore.properties`.

> 🔑 **Back up `release.jks` and its password off this machine** (password manager + a second
> safe copy). They are git-ignored — not in the repo, not on GitHub. Losing them means you can
> never ship an update once you enrol in Play App Signing with this upload key (Google can reset
> the upload key, but don't rely on it).

---

## 0. Prerequisites — the account rule that trips people up
- **Create a Google Play Developer account** ($25 one-time) at <https://play.google.com/console>.
  You'll complete **identity verification** (name, address, sometimes a document) — allow a day or two.
- ⚠️ **Personal accounts created recently must run a closed test with ≥12 testers for 14
  continuous days *before* Google unlocks the Production track.** Organization accounts are exempt.
  So your first upload realistically goes to a **testing track**, not straight to Production — plan
  for the 14-day window.

## 1. Create the app in Console
All apps → **Create app** → name "Asma al-Husna", default language, **App** (not game), **Free**,
accept declarations.

## 2. Push (upload) the AAB to a track
This is the actual "push to Play". Left menu → **Testing → Internal testing** (fastest) →
**Create new release**.
1. In **App bundles**, click **Upload** (or drag the file in) and pick:
   `app/build/outputs/bundle/release/app-release.aab`
2. First upload → **accept Play App Signing** when prompted. Google then holds the real
   app-signing key; your `release.jks` is only the *upload* key.
3. Add release notes → **Next** → **Save**.

The bundle is ~6.3 MB and already signed with the `asmaulhusna` upload key, so it's accepted.

> The **rollout button stays disabled** until the "App content" forms (§3) are green and the app
> exists under a verified developer account.

## 3. App content — the remaining Console blockers (#5, #6)
Left menu → **App content**. Tailored answers for this app:

**Privacy policy** → paste `https://sites.google.com/view/asma-al-husna/home`

**Data safety** — based on how the app actually behaves:
- Favorites / zikir counts / settings are **stored on-device only** and (optionally)
  auto-backed-up to the *user's own* Google account → **not** "collected" by you.
- **The app includes Firebase Cloud Messaging (FCM).** FCM registers a device token with Google
  to enable push. Honest declaration:
  - *Does your app collect or share user data?* → **Yes**
  - **Device or other IDs** → **Collected**, purpose **App functionality** (messaging),
    **not shared**, **not used for tracking**.
  - Everything else → **No**.
- *Alternative:* if you don't plan to send announcements, remove FCM from the app — then the
  honest answer becomes a clean **"No data collected."**

**Content rating** (IARC questionnaire): category **Reference/Education**; answer **No** to
violence, sexual content, profanity, gambling, drugs, and user-to-user communication. Result:
**Everyone / PEGI 3**.

**Target audience & content:** choose age groups (**13+** is simplest; "designed for children"
triggers stricter Families policy — avoid unless intended). **Ads: No. News app: No. Government app: No.**

## 4. Store listing
Left menu → **Main store listing**.
- **Short description** (≤80 chars) + **full description** (≤4000 chars).
- **Graphics required:**
  - App icon **512×512** PNG.
  - **Feature graphic 1024×500** PNG (required).
  - **Phone screenshots** — 2 to 8 (capture the home grid, a name detail, the Virtues screen,
    Settings from the app on a device/emulator).
- **App category:** Books & Reference (or Lifestyle).

## 5. Roll out
Testing track → **Review release → Start rollout to Internal testing**. Add your ≥12 testers
(an email list or a Google Group), keep the test live **14 days**, then **Production → Create
release** (reuse the same AAB) → submit for review. Review typically takes hours to a few days.

---

## Rebuilding the signed AAB
```bash
./gradlew bundleRelease
# -> app/build/outputs/bundle/release/app-release.aab (signed, R8-shrunk)
```
Bump `versionCode` (and usually `versionName`) in `app/build.gradle.kts` before every new upload —
Play rejects a bundle whose `versionCode` already exists.

## Optional: automated push for future updates
After the app exists in Console and you've done **one manual upload**, you can push from the
terminal instead of the browser:
- **Gradle Play Publisher** plugin → `./gradlew publishReleaseBundle`
- **fastlane** → `fastlane supply --aab app-release.aab`

Both need a **Google Cloud service account** with the Play Developer API enabled and granted
access in Console (Users & permissions) — ~15 min of setup, worthwhile only if you ship often.
**Skip it for the first release**; do that one by hand (§2).

---

## Quick status
| Handled in the repo | On you (Console / manual) |
|---|---|
| Signed AAB, R8 shrink, permissions | Developer account + identity check |
| Privacy policy (written + hosted) | Data Safety, Content Rating forms (§3) |
| Backup rules, authenticity disclaimer | Screenshots + feature graphic (§4) |
| Offline audio, no token upload | The ≥12-tester / 14-day closed test |
