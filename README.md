# Self-Care App 🌱✨

A gamified self-care Android application that helps users build healthy daily habits while taking care of their virtual pet 🚀🐾.

# ✨ Features

This app helps users build healthy daily habits in a fun and engaging way. It includes four main daily goals: Water, Steps, Sleep, and Focus. Users can set their targets, track their progress, and earn fuel whenever they complete a goal.

Space Mini-Game: With the fuel they collect, users can play a mini-game where their virtual pet explores the galaxy, collecting stars and avoiding asteroids.

Customization: Users can personalize their experience by changing their pet’s name, color, and preferred app language.

Progress Tracking: Encourages consistency by showing daily and overall achievements.

DinoBot AI Assistant (New!): A smart, interactive baby dinosaur chat companion that knows your daily progress and motivates you to reach your goals.

# 🤖 DinoBot: The AI Companion

The app features DinoBot, an intelligent AI chatbot powered by the Google Gemini API (Gemini 3 Flash). DinoBot is not just a regular chatbot; it is fully integrated with the user's personal health data.

Context-Aware: DinoBot accesses your real-time data (water intake, sleep duration, focus minutes, and steps) via SharedPreferences.

Personalized Coaching: If you haven't drank enough water, DinoBot will remind you with a friendly "Rawrr!" to stay hydrated.

Multi-language Support: It automatically responds in the same language you use to chat.

Dynamic Character: DinoBot has a unique personality—it’s a wise, cheerful baby dinosaur that never barks but always cheers you up!

# 🛠️ Tech Stack

* Language: Java (Android)
* IDE: Android Studio
* AI Engine: Google Gemini API (generativeai:0.9.0)
* Uses device sensors:

  * Step counter for activity tracking
  * Light & motion sensors for sleep detection and minigame

# 🚀 How It Works

* Completing each goal gives fuel
* Fuel can be used to play the space mini-game
* If the sleep goal detects both light and motion, it marks the user as awake
* During Focus Mode, the app prevents returning to the main screen until the timer is stopped
* Smart Chat: The AI analyzes your goalsSummary string to provide data-driven feedback on your lifestyle.

# 📥 Installation

1. Clone the repo:

   git clone https://github.com/esraevrim/App_pet

2. API Key Setup:

 Obtain a Gemini API key from Google AI Studio.
 Add your API key to PetChatActivity.java.
  
3. Open in Android Studio
4. Run on an emulator or physical device

# 👩‍💻 Authors

* Esra Evrim Özer
* Feyza Karaoğlu
* Hatice Beyza Ceyhan

