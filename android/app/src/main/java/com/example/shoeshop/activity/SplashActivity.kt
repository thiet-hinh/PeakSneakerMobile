package com.example.shoeshop.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.shoeshop.R
import com.example.shoeshop.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // Khởi chạy các animation
        findViewById<View>(R.id.cardCenter).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_up))
        findViewById<View>(R.id.layoutLoading).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_up))
        findViewById<View>(R.id.imgLogo).startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse_scale))

        // Điều hướng sau 2 giây
        Handler(Looper.getMainLooper()).postDelayed({
            //Kiểm tra instance của firebase
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                val localUser = PrefManager.getUser(applicationContext)
                //Kiểm thông tin User local có còn không
                if (localUser != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}