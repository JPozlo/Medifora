package com.misolova.medifora.domain.model

import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

data class User(
    val userInfo: UserInfo,
    val questions: List<Question>? = null,
    val answers: List<Answer>? = null
)

@Parcelize
data class UserInfo(
    val userId: String,
    val name: String,
    val email: String,
    val photo: String?,
    val password: String,
    val accountCreatedAt: Long
) : Parcelable {

    companion object {
        @ExperimentalTime
        fun DocumentSnapshot.toUser(): User? {
            try {
                val name = getString("name")!!
                val imageUrl = getString("profileImage")!!
                val email = getString("email")!!
                val password = getString("password")
                val createdAt =
                    getTimestamp("accountCreatedAt")?.seconds?.milliseconds?.toLongMilliseconds()!!
                return User(
                    UserInfo(
                        userId = id,
                        name = name,
                        email = email,
                        photo = imageUrl,
                        password = password!!,
                        accountCreatedAt = createdAt
                    )
                )
            } catch (e: Exception) {
                Timber.d("Error converting user profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("userID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }

        private const val TAG = "User"
    }


}