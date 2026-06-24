package com.example.shoeshop.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.shoeshop.R
import com.example.shoeshop.adapter.PagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var menus: List<LinearLayout>
    private lateinit var icons: List<ImageView>
    private lateinit var texts: List<TextView>
    private lateinit var txtNotificationBadge: TextView
    private lateinit var txtCartNumberItem: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        val imgCart = findViewById<ImageView>(R.id.imgCart)

        imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val txtLogo = findViewById<TextView>(R.id.txtLogo)
        txtLogo.post {
            val textWidth = txtLogo.paint.measureText(txtLogo.text.toString())

            txtLogo.paint.shader = LinearGradient(
                0f,
                0f,
                textWidth,
                0f,
                intArrayOf(
                    Color.parseColor("#396477"),
                    Color.parseColor("#C8E8FF")
                ),
                null,
                Shader.TileMode.CLAMP
            )

            txtLogo.invalidate()
        }

        txtNotificationBadge = findViewById(R.id.txtNotificationBadge)
        updateNumberNotification(txtNotificationBadge,0)

        txtCartNumberItem = findViewById(R.id.txtCartNumberItem)
        updateNumberNotification(txtCartNumberItem,0)



        viewPager = findViewById(R.id.viewPager)

        menus = listOf(
            findViewById(R.id.menuHome),
            findViewById(R.id.menuStore),
            findViewById(R.id.menuOrder),
            findViewById(R.id.menuProfile)
        )

        icons = listOf(
            findViewById(R.id.iconHome),
            findViewById(R.id.iconStore),
            findViewById(R.id.iconOrder),
            findViewById(R.id.iconProfile)
        )

        texts = listOf(
            findViewById(R.id.txtHome),
            findViewById(R.id.txtStore),
            findViewById(R.id.txtOrder),
            findViewById(R.id.txtProfile)
        )

        viewPager.adapter = PagerAdapter(this)

        menus[0].setOnClickListener { viewPager.currentItem = 0 }
        menus[1].setOnClickListener { viewPager.currentItem = 1 }
        menus[2].setOnClickListener { viewPager.currentItem = 2 }
        menus[3].setOnClickListener { viewPager.currentItem = 3 }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateBottomMenu(position)
            }
        })

        updateBottomMenu(0)
    }

    private fun updateBottomMenu(position: Int) {
        val activeColor = Color.parseColor("#396477")
        val inactiveColor = Color.parseColor("#747878")

        menus.forEachIndexed { index, menu ->
            if (index == position) {
                menu.setBackgroundResource(R.drawable.bg_bottom_active)

                icons[index].setColorFilter(activeColor)
                texts[index].setTextColor(activeColor)

                menu.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(150)
                    .start()
            } else {
                menu.background = null

                icons[index].setColorFilter(inactiveColor)
                texts[index].setTextColor(inactiveColor)

                menu.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
        }
    }

    private fun updateNumberNotification(tv: TextView,count: Int) {
        if (count <= 0) {
            tv.visibility = View.GONE
            return
        }
        tv.visibility = View.VISIBLE
        tv.text = if (count > 99) "99+" else count.toString()
    }
}