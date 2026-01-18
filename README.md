# News Flow 📰

News Flow is a modern Android application that allows users to search, read, listen to, and save news articles using data retrieved from the News API.
Built with Jetpack Compose and MVVM architecture, the app focuses on accessibility, background playback, and user engagement through smart notifications and visual insights.

-------

### News Search 🔍

Search for the latest news articles via the News API

Clean and responsive UI built entirely with Jetpack Compose

### Save Articles 🔖 

Save articles for future reading

Saved articles are stored securely using Firebase

### Text-to-Speech Narration 🔊 

Listen to article summaries using Text to Speech

Narration continues when the app is minimised or closed

### Background & System Handling ⚙️ 

Foreground Service and Bound Service to maintain active narration

Broadcast Receiver detects incoming calls or competing audio and automatically stops narration

### Smart Notifications ⏰ 

Daily reading reminders using WorkManager

Notifications delivered via NotificationManager

### Reading Insights 📊 

Article preferences stored using Preference DataStore

Visual analytics using YCharts

Pie chart displaying read vs saved articles

-------

### Tech Stack 🛠 
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

### Architecture 🏗 

MVVM (Model–View–ViewModel) for clear separation of concerns

Unidirectional data flow with Compose state

Lifecycle-aware background services

Modular and scalable codebase

-------

## Getting Started 🚀

Clone the repository:

git clone https://github.com/Mosfeq/NewsFlow

Open the project in Android Studio

Add your News API key

Configure Firebase (google-services.json)

Build and run on an emulator or physical device

-------

Below is a video of the running application:


