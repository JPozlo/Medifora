package com.misolova.medifora.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import kotlin.time.ExperimentalTime

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
    val accountCreatedAt: Timestamp
) : Parcelable {

    companion object {
        @ExperimentalTime
        fun DocumentSnapshot.toUserInfo(): UserInfo?{
            return try {
                val name = getString("name")!!
                val email = getString("email")!!
                val createdAt = getTimestamp("accountCreatedAt")!!
                UserInfo(userId = id, name = name, email = email, accountCreatedAt = createdAt)
            }catch (e: Exception) {
                Timber.d("Error converting user profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("userID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        fun DocumentSnapshot.toUser(): User? {
            try {
                val name = getString("name")!!
                val imageUrl = getString("profileImage")!!
                val email = getString("email")!!
                val createdAt =
                    getTimestamp("accountCreatedAt")!!
                return User(
                    UserInfo(
                        userId = id,
                        name = name,
                        email = email,
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