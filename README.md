# 📰 News Flow

News Flow is a modern Android application that allows users to search, read, listen to, and save news articles using data retrieved from the News API.
Built with Jetpack Compose and MVVM architecture, the app focuses on accessibility, background playback, and user engagement through smart notifications and visual insights.

-------

### News Search 🔍 

Search for the latest news articles via the News API

Clean and responsive UI built entirely with Jetpack Compose
<br>

### Save Articles 🔖 

Save articles for future reading

Saved articles are stored securely using Firebase
<br>

### Text-to-Speech Narration 🔊 

Listen to article summaries using Text to Speech

Narration continues when the app is minimised or closed
<br>

### Background & System Handling ⚙️ 

Foreground Service and Bound Service to maintain active narration

Broadcast Receiver detects incoming calls or competing audio and automatically stops narration
<br>

### Smart Notifications ⏰ 

Daily reading reminders using WorkManager

Notifications delivered via NotificationManager
<br>

### Reading Insights 📊 

Article preferences stored using Preference DataStore

Visual analytics using YCharts

Pie chart displaying read vs saved articles

-------

## 🛠 Tech Stack
#### Android

- Kotlin

- Jetpack Compose

- MVVM Architecture

- StateFlow / ViewModel

- Text to Speech (TTS)

#### System & Background Components

- Foreground Service

- Bound Service

- Broadcast Receiver

- WorkManager

- NotificationManager

#### Backend & Libraries

- News API – news data source

- Firebase – saved articles

- Preference DataStore – user preferences

- YCharts – data visualisation

-------

## 🏗 Architecture

MVVM (Model–View–ViewModel) for clear separation of concerns

Unidirectional data flow with Compose state

Lifecycle-aware background services

Modular and scalable codebase

-------

## 🚀 Getting Started

Clone the repository:

git clone https://github.com/Mosfeq/NewsFlow

Open the project in Android Studio

Add your News API key

Configure Firebase (google-services.json)

Build and run on an emulator or physical device

-------

## 🔗 App Demo 

A short demo of News Flow showcasing article search, text-to-speech narration, background playback, and reading insights is available on LinkedIn:

👉 **Watch the demo on LinkedIn:**
https://www.linkedin.com/posts/activity-7419087322660532224-Ad-Y?utm_source=share&utm_medium=member_desktop&rcm=ACoAADJ-7aYB4zbEAPnyTuZl4qx7zSSVG5tLCos

-------

Below are screenshots of the running application:
<br><br>

<img src="https://github.com/user-attachments/assets/c06ddd5d-044b-4739-a53e-4f54ada0cc1e" width="250"/>
<img src="https://github.com/user-attachments/assets/eb3c993c-e088-4b94-baf6-106eb112613b" width="250"/>
<img src="https://github.com/user-attachments/assets/fc8949db-5916-4cde-bb85-671c6f40d00c" width="250"/>
<img src="https://github.com/user-attachments/assets/7912f1d7-80a0-46a0-956b-a53f36bb7136" width="250"/>
<img src="https://github.com/user-attachments/assets/5284649c-2c93-4272-b010-2089358787fb" width="250"/>
<img src="https://github.com/user-attachments/assets/9805398e-248e-4b44-880d-79d1bfcf13a7" width="250"/>
<img src="https://github.com/user-attachments/assets/fc509eda-d520-4338-ac45-8dacf4d6cda1" width="250"/>
<img src="https://github.com/user-attachments/assets/7a6272c5-7f90-4ffa-b069-d10341008b87" width="250"/>
<img src="https://github.com/user-attachments/assets/8796cd7f-acb3-4a7a-8a5c-d74ef892c909" width="250"/>
<img src="https://github.com/user-attachments/assets/57567f20-b8de-4447-9647-396b7afd313a" width="250"/>
<img src="https://github.com/user-attachments/assets/7ce9cd5a-9f2b-45a8-8702-bc0ab85d6d43" width="250"/>
<img src="https://github.com/user-attachments/assets/956df421-72e8-4f19-9e68-f0d8870270f7" width="250"/>
<img src="https://github.com/user-attachments/assets/8f392f3c-8701-40ab-9d90-277b8f298cfe" width="250"/>


