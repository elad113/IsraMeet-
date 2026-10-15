# CodeQuest Android

Standalone Android project replacing the current contents of this repository. This is an initial APK-buildable shell, not yet a feature-complete port of the CodeQuest website.

## Build APK
Push to `main` or run the **Build CodeQuest APK** workflow manually. Download the `codequest-debug-apk` artifact from the workflow run.

## AI configuration and secrets
Copy `ai-config.properties.example` to `ai-config.properties` for local development. This file is git-ignored. Never commit API keys or embed production keys in a distributed APK; APK contents can be extracted. For production AI, put Groq and Gemini keys on a backend you control. Configure Groq as primary and Gemini as fallback on that backend.

## Porting status
This commit establishes the Android shell and CI build pipeline. It does not yet port all CodeQuest web functionality, its lessons, AI orchestration, cloud sync, or AppDeploy-specific backend. Those require the original website source and replacement backend APIs.
