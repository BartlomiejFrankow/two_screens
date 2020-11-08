package com.example.twoscreens.firebase

import android.content.Context
import com.example.twoscreens.R
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.FirebaseFirestoreException.Code.*

class FirebaseError(private val context: Context) {

    fun getMessage(errorCode: Code): String {
        return when (errorCode) {
            CANCELLED -> ErrorMessages.CANCELLED.message.getString()
            UNKNOWN -> ErrorMessages.UNKNOWN.message.getString()
            INVALID_ARGUMENT -> ErrorMessages.INVALID_ARGUMENT.message.getString()
            DEADLINE_EXCEEDED -> ErrorMessages.DEADLINE_EXCEEDED.message.getString()
            NOT_FOUND -> ErrorMessages.NOT_FOUND.message.getString()
            ALREADY_EXISTS -> ErrorMessages.ALREADY_EXISTS.message.getString()
            PERMISSION_DENIED -> ErrorMessages.PERMISSION_DENIED.message.getString()
            RESOURCE_EXHAUSTED -> ErrorMessages.RESOURCE_EXHAUSTED.message.getString()
            FAILED_PRECONDITION -> ErrorMessages.FAILED_PRECONDITION.message.getString()
            ABORTED -> ErrorMessages.ABORTED.message.getString()
            OUT_OF_RANGE -> ErrorMessages.OUT_OF_RANGE.message.getString()
            UNIMPLEMENTED -> ErrorMessages.UNIMPLEMENTED.message.getString()
            INTERNAL -> ErrorMessages.INTERNAL.message.getString()
            UNAVAILABLE -> ErrorMessages.UNAVAILABLE.message.getString()
            DATA_LOSS -> ErrorMessages.DATA_LOSS.message.getString()
            UNAUTHENTICATED -> ErrorMessages.UNAUTHENTICATED.message.getString()
            OK -> ErrorMessages.OK.message.getString()
        }
    }

    private fun Int.getString() = context.getString(this)

}

enum class ErrorMessages(val message: Int) {
    CANCELLED(R.string.error_cancelled),
    UNKNOWN(R.string.error_unknown),
    INVALID_ARGUMENT(R.string.error_invalid_argument),
    NOT_FOUND(R.string.error_not_found),
    DEADLINE_EXCEEDED(R.string.error_deadline_exceeded),
    ALREADY_EXISTS(R.string.error_already_exist),
    PERMISSION_DENIED(R.string.error_permission_denied),
    RESOURCE_EXHAUSTED(R.string.error_resource_exhausted),
    FAILED_PRECONDITION(R.string.error_failed_preconditions),
    ABORTED(R.string.error_aborted),
    OUT_OF_RANGE(R.string.error_out_of_range),
    UNIMPLEMENTED(R.string.error_unimplemented),
    INTERNAL(R.string.error_internal),
    UNAVAILABLE(R.string.error_unavailable),
    DATA_LOSS(R.string.error_data_loss),
    UNAUTHENTICATED(R.string.error_unauthenticated),
    OK(R.string.error_unrecognized)
}
