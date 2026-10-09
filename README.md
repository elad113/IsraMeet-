# IsraMeet Android

IsraMeet is a Hebrew RTL Android/WebView meeting app project built with Vite and Capacitor.

## Current implementation

- Animated IsraMeet letter-by-letter splash screen.
- Home, meetings list, calendar, messages, profile and settings screens.
- Create a room and share its meeting code; schedule a meeting with a local Android notification.
- Camera/microphone access and live local camera preview.
- PeerJS/WebRTC room signaling and audio/video calls through a host room code; participants use the same code and the host must remain online.
- Meeting chat relayed through the room host.
- Local meeting recording via MediaRecorder, download, and IndexedDB storage on the device.
- Local profile registration/login, with a hashed password stored on the device.
- Android camera, microphone, notification and alarm permissions.
- Custom notification icon and three-second generated notification sound.
- Two bottom-bar shortcuts: create meeting and join meeting.

## Build the APK

1. Open **Actions**.
2. Select **Build IsraMeet Android APK**.
3. Wait for a successful run.
4. Download `IsraMeet-Android-APK` from the run's Artifacts section.

The artifact is a debug APK for testing.

## Important limitations

- Account registration is currently device-local. It does not create cloud users or a shared user database. A production multi-user database/auth system requires a configured backend (for example, Supabase/Firebase) and credentials/secrets.
- Live calling depends on internet access, Android camera/microphone permissions, and the PeerJS public signaling service. Network/firewall/NAT conditions may prevent calls. Test on two real devices.
- The host must start the room before others join. The host acts as a relay for participant discovery and chat.
- Screen capture depends on Android/WebView support and user-granted system permissions.
- Recording is saved in the app's local IndexedDB and a downloaded WebM file. Browser/WebView storage can be cleared by Android or the user. Obtain consent before recording.
- This is a reconstruction from a compiled reference APK, not the original source. It cannot guarantee every feature from the older app is restored without the original backend and source.

## Repository

https://github.com/elad113/IsraMeet-
