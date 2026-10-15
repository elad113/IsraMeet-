# CodeQuest Android
This repository packages the CodeQuest React website into an Android APK. The site is built with Vite and copied into Android assets, then loaded locally by WebView. No AppDeploy client is required at runtime for the currently ported local experience.

## Build
GitHub Actions builds the web app and packages it into `codequest-website-apk`.

## Current limitations
Profile progress is stored locally on the device. AI lesson generation and hints use the website's built-in local lesson fallback because the AppDeploy API dependency has been removed. Remote sync and Groq/Gemini are not connected yet.