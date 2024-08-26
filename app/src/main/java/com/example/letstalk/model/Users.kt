package com.example.letstalk.model

class Users {
    var uid: String = ""
    var name: String = ""
    var phone: String = ""
    var userProfile: String = ""
    var lastMsg: String = ""
    var lastTime: String = ""

    constructor()
    constructor(
        uid: String,
        name: String,
        phone: String,
        userProfile: String,
        lastMsg: String,
        lastTime: String
    ) {
        this.uid = uid
        this.name = name
        this.phone = phone
        this.userProfile = userProfile
        this.lastTime = lastTime
        this.lastMsg = lastMsg
    }
}




