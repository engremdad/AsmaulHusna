package com.islamic.asmaulhusna.notify

import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Registers this device's FCM token with your backend so it can be targeted by
 * push campaigns. Called from [AsmaulHusnaMessagingService.onNewToken] whenever the
 * token rotates, and can be called on app start via [refreshAndUpload].
 *
 * TODO: point BASE_URL at your real API and secure the endpoint (auth header, etc.).
 */
object TokenUploader {

    // No real backend yet: uploading the device token would send an identifier to a
    // placeholder server (privacy/Play-policy risk). Uploads are disabled until BASE_URL
    // points at a secured endpoint you own — then set BACKEND_CONFIGURED = true.
    private const val BACKEND_CONFIGURED = false

    // Trailing slash is required by Retrofit. Replace with your backend.
    private const val BASE_URL = "https://api.example.com/"
    private const val TAG = "FCM"

    /** Payload sent to the backend. Extend with a user id once the user is signed in. */
    data class TokenRegistration(
        val token: String,
        val platform: String = "android",
        val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}",
    )

    private interface PushApi {
        @POST("v1/devices")
        suspend fun registerToken(@Body body: TokenRegistration)
    }

    // Survives beyond a single call so uploads finish even if the service is torn down.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val api: PushApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PushApi::class.java)
    }

    /** Send a known token to the backend. Safe to call from any thread. No-op until a backend is configured. */
    fun upload(token: String) {
        if (!BACKEND_CONFIGURED) {
            Log.d(TAG, "Token upload skipped — no backend configured")
            return
        }
        scope.launch {
            try {
                api.registerToken(TokenRegistration(token = token))
                Log.d(TAG, "FCM token registered with backend")
            } catch (e: Exception) {
                // Network/backend failure — FCM will call onNewToken again on the next
                // rotation, and refreshAndUpload() retries on the next app start.
                Log.w(TAG, "Failed to register FCM token", e)
            }
        }
    }

    /**
     * Fetch the current token from FCM and upload it. Call this from your Activity's
     * onCreate (e.g. after the user grants notification permission or signs in), since
     * onNewToken only fires when the token actually changes.
     */
    fun refreshAndUpload() {
        if (!BACKEND_CONFIGURED) return
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }
                upload(task.result)
            }
    }
}
