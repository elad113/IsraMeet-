# IsraMeet — rebuilt Android source

This repository is now the active source baseline for IsraMeet, reconstructed from the latest available reference APK (com.isrameet.app, app name IsraMeet).

## What is included

- Hebrew RTL mobile UI with the five bottom navigation destinations: בית, פגישות, יומן, הודעות, פרופיל.
- Home, meeting list, calendar, profile, create/join/schedule flows, and a meeting-room screen.
- Local persistence for meetings and profile using device storage.
- Capacitor local-notification scheduling for future meetings and notification-tap navigation into the matching meeting.
- Android APK build workflow in GitHub Actions.

## Build an APK

1. Open the Actions tab in this repository.
2. Select **Build IsraMeet Android APK**.
3. Run the workflow or push a change to main.
4. Download the IsraMeet-Android-APK artifact from a successful run.

The workflow generates the Android platform from Capacitor and builds a debug APK. It does not commit generated Android build outputs to source control.

## Accuracy and limitations

The original APK contains compiled/minified JavaScript and CSS rather than the original editable project. This repository reconstructs the main UI and app flows from that reference; it is **not a byte-for-byte or fully feature-equivalent copy**. The current meeting room provides the UI shell, but live multi-device audio/video, server-backed accounts/chat, real screen broadcasting, and host-controlled meeting features require backend/media services that were not recoverable from the APK alone. Scheduled notifications depend on Android notification permissions and device power-management behavior.

## Reference

- Latest reference APK was extracted from the GitHub Actions artifact in elad113/Vicationfly-Android; the APK's internal app identity is com.isrameet.app.
- Active source repository: https://github.com/elad113/IsraMeet-
