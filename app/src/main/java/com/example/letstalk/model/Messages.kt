package com.example.letstalk.model

class Messages {
    var senderId: String = ""
    var reciverId: String = ""
    var message: String = ""
    var timeStamp: String = ""
    var attachImage: String = ""
    var userImage: String = ""

    constructor()
    constructor(
        senderId: String,
        message: String,
        timeStamp: String,
        reciverId: String,
        userImage: String
    ) {
        this.reciverId = reciverId
        this.timeStamp = timeStamp
        this.message = message
        this.senderId = senderId
        this.userImage = userImage
    }
}

