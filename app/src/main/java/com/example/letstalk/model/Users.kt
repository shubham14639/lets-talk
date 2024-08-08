package com.example.letstalk.model

class Users {
    var uid: String = ""
    var name: String = ""
    var phone: String = ""
    var userProfile: String = ""

    constructor()
    constructor(
        uid: String,
        name: String,
        phone: String,
        userProfile: String
    ) {
        this.uid = uid
        this.name = name
        this.phone = phone
        this.userProfile = userProfile
    }
}




