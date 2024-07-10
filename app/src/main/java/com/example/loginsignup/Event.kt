package com.example.loginsignup

data class Event(
    var eventName: String = "",
    var phoneNumber: String = "",
    var sportsName: String = "",
    var sportCategory: String = "",
    var date: String = "",
    var location: String = "",
    var email: String = "", // Changed from emailEditText to email
    var callButton: String = "",
    var chatButton: String = "",
    var age: Int = 0,
    var imageUrl: String = ""

)

