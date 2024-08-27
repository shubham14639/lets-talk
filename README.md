 <h1> 📱 Let's Talk </h1>

Overview

This repository contains a Chat Application built using Kotlin, leveraging the MVVM (Model-View-ViewModel) architecture pattern. The app integrates with Firebase to provide essential functionalities such as Phone Authentication, Real-time Chat using Firebase Realtime Database, and Push Notifications via Firebase Cloud Messaging (FCM).

<h1>✨ Features </h1>

🔐 User Authentication: Authenticate users using their phone numbers with Firebase Authentication.  
💬 Real-time Chat: Exchange messages in real-time using Firebase Realtime Database.   
📲 Push Notifications: Receive notifications for new messages via Firebase Cloud Messaging.    
🏗️ MVVM Architecture: Clean and maintainable code structure with a separation of concerns.   
🔥 Firebase Integration: Seamless integration with Firebase services for backend support.   

<h1> 🛠 Tech Stack </h1>  

Kotlin: Programming language used for Android development.   
MVVM Pattern: Design pattern that separates the UI logic (View), business logic (ViewModel), and data management (Model).   
Firebase Authentication: Handles user authentication via phone numbers.   
Firebase Realtime Database: Manages real-time data exchange between users.  
Firebase Cloud Messaging (FCM): Sends push notifications to users about new messages.  

 (ViewModel), and data management (Model).   
Firebase Authentication: Handles user authentication via phone numbers.   
Firebase Realtime Database: Manages real-time data exchange between users.   
Firebase Cloud Messaging (FCM): Sends push notifications to users about new messages.    

<h1> 🚀 Getting Started </h1>    
Prerequisites    
Android Studio: Ensure you have the latest version installed.    
Firebase Account: Set up a Firebase project and add an Android app to it.    
Setup Firebase   
Add Firebase to Your Project:    

Go to Firebase Console.    
Create a new project or use an existing one.     
Add an Android app to your Firebase project.       
Download the google-services.json file and place it in your project's app/ directory.       
Enable Authentication:    

Navigate to the Authentication section in the Firebase console.     
Enable the Phone authentication method.     
Set Up Realtime Database:    

In the Firebase console, go to the Realtime Database section.      
Create a database and set the rules to allow authenticated users to read and write data:     
{  
  "rules": {   
    ".read": "auth != null",   
    ".write": "auth != null"   
    }   
}  


Enable Firebase Cloud Messaging:     
Go to the Firebase Cloud Messaging section and enable it.     

Installation   
Clone the repository:     
bash     
Copy code     
git clone https://github.com/shubhamdevgupta/lets-talk.git    
Open the project in Android Studio.    

Sync the project with Gradle to install dependencies.    

Build and run the app on an emulator or physical device.       

<h1>📱 Usage  </h1> 

Sign In: Users can sign in using their phone number. Firebase will send a verification code to authenticate.   
Chat: Users can send and receive messages in real-time.  
Notifications: Users will receive push notifications for new messages even when the app is in the background.  

<h1> 🗂️ Project Structure </h1>

model/: Contains data classes and repository classes that handle data operations.   
view/: Contains UI components such as Activities and Fragments.   
viewmodel/: Contains ViewModel classes that manage the data for the UI.   
network/: Contains Firebase-related classes for authentication, database, and messaging services.   

<h1> 🤝 Contributing </h1>
Contributions are welcome! Please fork this repository and submit a pull request for any changes or improvements.  

<h1> 📝 License </h1>
This project is licensed under the MIT License. See the LICENSE file for more information.   


