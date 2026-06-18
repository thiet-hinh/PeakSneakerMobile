package com.example.shoeshop

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun testFirebaseConnection(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: "Unknown UID"

                Log.d("FIREBASE_TEST", "Connected. UID = $uid")

                onSuccess(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_TEST", "Connection failed", exception)

                onError(exception.message ?: "Unknown error")
            }
    }
}