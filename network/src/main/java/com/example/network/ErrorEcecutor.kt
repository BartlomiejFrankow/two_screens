package com.example.network

import com.example.domain.ErrorMessage
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.FirebaseFirestoreException.Code.*

class ErrorExecutor(private val errorMessage: ErrorMessage) {

    fun execute(errorCode: Code) {
        val resource = when (errorCode) {
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
            else -> ErrorMessages.UNRECOGNIZED.message
        }

        errorMessage.show(resource)
    }

    fun execute(message: String?) = errorMessage.show(message)

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
    UNRECOGNIZED(R.string.error_unrecognized)
}
