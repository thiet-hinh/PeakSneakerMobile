package com.example.shoeshop

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Checking Firebase..."
        setContentView(textView)

        FirebaseApp.initializeApp(this)

        authRepository = AuthRepository()

        authRepository.testFirebaseConnection(
            onSuccess = { uid ->
                textView.text = """
                    Firebase Connected ✅
                    
                    UID:
                    $uid
                """.trimIndent()

                Log.d("FIREBASE_TEST", "Success")
            },
            onError = { error ->
                textView.text = """
                    Firebase Failed ❌
                    
                    Error:
                    $error
                """.trimIndent()

                Log.e("FIREBASE_TEST", error)
            }
        )
    }
}