package com.example.letstalk.model

class Messages {
    var senderId: String = ""
    var reciverId: String = ""
    var message: String = ""
    var timeStamp: String = ""

    constructor()
    constructor(
        senderId: String,
        message: String,
        timeStamp: String,
        reciverId: String
    ) {
        this.reciverId = reciverId
        this.timeStamp = timeStamp
        this.message = message
        this.senderId = senderId
    }
}

