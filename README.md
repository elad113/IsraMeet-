# IsraMeet — baseline recovery

This repository is being used as the new baseline for the latest IsraMeet Android APK.

## Baseline identity verified from the APK

- Android application ID: `com.isrameet.app`
- App name: `IsraMeet`
- Web runtime: Capacitor
- UI direction/language: Hebrew, right-to-left (with English strings also bundled)
- Bundled web entry point: `assets/public/index.html`
- Bundled UI files in the APK: `assets/public/assets/index-C65rsY_N.js` and `assets/public/assets/index-Cpd-bfr9.css`

The packaged UI bundle contains screens and strings for meetings, calendar, messages, profile/account, joining and creating meetings, and scheduled meeting notifications. The APK is the reference artifact for matching the current appearance and behavior.

## Important source-recovery note

The APK contains compiled Android code and bundled/minified web assets, not the original editable project source. This repository is initialized as the intended baseline, but it is **not yet a complete, rebuildable Android source project**. To claim a true 1:1 source copy, the bundled assets and Android project structure still need to be recovered and checked, and the resulting build must be compared with the reference APK. No feature or visual match should be considered verified until that work is completed.

## Reference

- Original APK build source/artifact was found in the `elad113/Vicationfly-Android` workflow artifact. The package identity inside the APK itself identifies the app as `com.isrameet.app` / `IsraMeet`.
- This repository is the new intended source-of-truth location: https://github.com/elad113/IsraMeet-

## Next steps

1. Recover the packaged web assets and Android manifest/resources from the reference APK.
2. Reconstruct a maintainable Capacitor/Android project around those assets.
3. Build and test the APK, including notifications and scheduled meeting navigation.
4. Compare the rebuilt app against the reference before calling it a 1:1 reproduction.
