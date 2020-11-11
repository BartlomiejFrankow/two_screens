package com.example.network

import com.google.firebase.Timestamp

fun Timestamp.toMilli() = this.seconds * 1000
