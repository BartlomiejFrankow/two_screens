package com.example.twoscreens.firebase

import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.FirebaseFirestoreException.Code.*

class FirebaseError {

    fun getMessage(errorCode: Code): String {
        return when (errorCode) {
            CANCELLED -> ErrorMessages.CANCELLED.message
            UNKNOWN -> ErrorMessages.UNKNOWN.message
            INVALID_ARGUMENT -> ErrorMessages.INVALID_ARGUMENT.message
            DEADLINE_EXCEEDED -> ErrorMessages.DEADLINE_EXCEEDED.message
            NOT_FOUND -> ErrorMessages.NOT_FOUND.message
            ALREADY_EXISTS -> ErrorMessages.ALREADY_EXISTS.message
            PERMISSION_DENIED -> ErrorMessages.PERMISSION_DENIED.message
            RESOURCE_EXHAUSTED -> ErrorMessages.RESOURCE_EXHAUSTED.message
            FAILED_PRECONDITION -> ErrorMessages.FAILED_PRECONDITION.message
            ABORTED -> ErrorMessages.ABORTED.message
            OUT_OF_RANGE -> ErrorMessages.OUT_OF_RANGE.message
            UNIMPLEMENTED -> ErrorMessages.UNIMPLEMENTED.message
            INTERNAL -> ErrorMessages.INTERNAL.message
            UNAVAILABLE -> ErrorMessages.UNAVAILABLE.message
            DATA_LOSS -> ErrorMessages.DATA_LOSS.message
            UNAUTHENTICATED -> ErrorMessages.UNAUTHENTICATED.message
            OK -> ErrorMessages.OK.message
        }
    }
}

// TODO move to string resources
enum class ErrorMessages(val message: String) {
    CANCELLED("Oops operation cancelled"),
    UNKNOWN("Oops something wen't wrong"),
    INVALID_ARGUMENT("Oops invalid argument"),
    NOT_FOUND("Oops request not found"),
    DEADLINE_EXCEEDED("Oops deadline expired"),
    ALREADY_EXISTS("Oops task already exist"),
    PERMISSION_DENIED("Oops permission denied"),
    RESOURCE_EXHAUSTED("Oops resource exhausted"),
    FAILED_PRECONDITION("Oops precondition failed"),
    ABORTED("Oops aborted"),
    OUT_OF_RANGE("Oops wrong range"),
    UNIMPLEMENTED("Oops method wasn't implemented"),
    INTERNAL("Oops something wen't wrong on server side"),
    UNAVAILABLE("Oops server is unavailable"),
    DATA_LOSS("Oops data loss"),
    UNAUTHENTICATED("Oops authentication expires"),
    OK("Unrecognized error")
}
