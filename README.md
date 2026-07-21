# InternAndroid

Androidインターン研修で使用するサンプルアプリです。

## 動作環境

- Windows 10 / 11
- Android Studio Panda 4 | 2025.3.4 Patch 1
- Android SDK Platform 36
- Android Gradle Plugin 9.2.1
- Gradle 9.4.1
- JDK 17（Android Studio同梱のJDKを使用）

## Android SDK

Android StudioのSDK Managerで、次の項目をインストールしてください。

- Android SDK Platform 36
- Android SDK Build-Tools
- Android SDK Platform-Tools
- Android Emulator

## エミュレータ

Device Managerから仮想デバイスを作成します。以下の構成で動作確認済みです。

- Device: Pixel 7
- System Image: API 36

作成した仮想デバイスを起動し、Android Studioからアプリを実行してください。

## コマンドラインでの確認

プロジェクトのルートで次のコマンドを実行します。

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest lintDebug
```
