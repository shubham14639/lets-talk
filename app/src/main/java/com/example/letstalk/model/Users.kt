package com.example.letstalk.model

class Users {
     var uid: String = ""
     var name: String = ""
     var phone: String = ""
     var imageUrl: String = ""
     var currentUser: String = ""

    constructor()
    constructor(
        uid: String,
        name: String,
        phone: String,
        imageUrl: String,
        currentUser: String
    ) {
        this.uid = uid
        this.name = name
        this.phone = phone
        this.imageUrl = imageUrl
        this.currentUser = currentUser
    }
}



