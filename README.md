# SNTF Alger-Thénia Timetable App

A Kotlin-based mobile application providing real-time access to the suburban train schedules for the Algiers metropolitan area.

##  Features
* **Full Main Line Support**: Complete schedules for the **Alger-Thénia** route.
* **Regional Connections**: Integrated data for specialized routes including:
    * **Zéralda** connections (Trains B100, B102, B124).
    * **El Affroun** connections (Trains B126, B152).

##  Tech Stack
* **Language**: Kotlin
* **Framework**: Android SDK (Jetpack Compose / XML)
* **Data Structure**: Custom `Train` and `Stop` data classes for efficient schedule parsing.

##  Installation
1. Clone this repository.
2. Open in **Android Studio**.
3. To change the app icon:
    * Right-click `res` > `New` > `Image Asset`.
    * Replace `ic_launcher` with your custom train icon.
4. Build and run on an emulator or physical device.

##  License
This project uses publicly available timetable data from SNTF.
