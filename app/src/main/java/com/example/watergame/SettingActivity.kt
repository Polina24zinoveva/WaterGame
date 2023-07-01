package com.example.watergame

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import de.hdodenhof.circleimageview.CircleImageView


class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)

        val darkTheme = sharedPref.getBoolean("darkTheme", false)
        val switch : SwitchCompat = findViewById(R.id.myThemeSwitch)
        switch.isChecked = darkTheme

        val settingLayout : ConstraintLayout = findViewById(R.id.settingLayout)
        val underSwitchCL : ConstraintLayout = findViewById(R.id.underSwitchCL)
        val settingTW : TextView = findViewById(R.id.settingTW)
        val darkThemeTW : TextView = findViewById(R.id.darkThemeTW)
        val colorTW : TextView = findViewById(R.id.uraTW)
        val openIW : ImageView = findViewById(R.id.openIW)

        if (darkTheme) {
            settingLayout.setBackgroundColor(Color.BLACK)
            underSwitchCL.setBackgroundResource(R.drawable.rounded_white_rectangle)
            settingTW.setTextColor(Color.WHITE)
            darkThemeTW.setTextColor(Color.WHITE)
            colorTW.setTextColor(Color.WHITE)
            openIW.setImageResource(R.drawable.open_for_dark_theme)
        }
        else{
            settingLayout.setBackgroundColor(Color.WHITE)
            underSwitchCL.setBackgroundResource(R.color.white)
            settingTW.setTextColor(Color.BLACK)
            darkThemeTW.setTextColor(Color.BLACK)
            colorTW.setTextColor(Color.BLACK)
            openIW.setImageResource(R.drawable.open)
        }

    }

    fun onCancelClick(view: View?) {
        val resultCode = Activity.RESULT_OK
        val resultIntent = Intent()

        setResult(resultCode, resultIntent)
        finish()
    }

    fun onColors(view: View) {
        val blueIW : ImageView = findViewById(R.id.blueIW)
        if (blueIW.isClickable){
            setInVisibleAndNotClickable()
        }
        else{
            setVisibleAndClickable()
        }
    }

    private fun setVisibleAndClickable(){
        val blueIW : CircleImageView = findViewById(R.id.blueIW)
        val greenIW : CircleImageView = findViewById(R.id.greenIW)
        val redIW : CircleImageView = findViewById(R.id.redIW)
        val pinkIW : CircleImageView = findViewById(R.id.pinkIW)
        val rainbowIW : CircleImageView = findViewById(R.id.rainbowIW)
        val layoutWithColors : LinearLayout = findViewById(R.id.layoutWithColors)


        blueIW.isClickable = true
        blueIW.isVisible = true
        greenIW.isClickable = true
        greenIW.isVisible = true
        redIW.isClickable = true
        redIW.isVisible = true
        pinkIW.isClickable = true
        pinkIW.isVisible = true
        rainbowIW.isClickable = true
        rainbowIW.isVisible = true

        layoutWithColors.isVisible = true

        strokeDesiredCircle()

    }

    private fun setInVisibleAndNotClickable(){
        val blueIW : ImageView = findViewById(R.id.blueIW)
        val greenIW : ImageView = findViewById(R.id.greenIW)
        val redIW : ImageView = findViewById(R.id.redIW)
        val pinkIW : ImageView = findViewById(R.id.pinkIW)
        val rainbowIW : ImageView = findViewById(R.id.rainbowIW)
        val layoutWithColors : LinearLayout = findViewById(R.id.layoutWithColors)


        blueIW.isClickable = false
        blueIW.isVisible = false
        greenIW.isClickable = false
        greenIW.isVisible = false
        redIW.isClickable = false
        redIW.isVisible = false
        pinkIW.isClickable = false
        pinkIW.isVisible = false
        rainbowIW.isClickable = false
        rainbowIW.isVisible = false

        layoutWithColors.isVisible = false

    }

    @SuppressLint("ResourceAsColor")
    private fun strokeDesiredCircle(){
        val blueIW : CircleImageView = findViewById(R.id.blueIW)
        val greenIW : CircleImageView = findViewById(R.id.greenIW)
        val redIW : CircleImageView = findViewById(R.id.redIW)
        val pinkIW : CircleImageView = findViewById(R.id.pinkIW)
        val rainbowIW : CircleImageView = findViewById(R.id.rainbowIW)

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getString("style(string)", "")
        val darkTheme = sharedPref.getBoolean("darkTheme", false)
        val borderColor = if (darkTheme){
            Color.WHITE
        } else{
            Color.BLACK
        }

        when (style) {
            "blue" -> {
                blueIW.borderWidth = 7
                blueIW.borderColor =  borderColor
            }
            "green" -> {
                greenIW.borderWidth = 7
                greenIW.borderColor =  borderColor
            }
            "red" -> {
                redIW.borderWidth = 7
                redIW.borderColor =  borderColor
            }
            "pink" -> {
                pinkIW.borderWidth = 7
                pinkIW.borderColor =  borderColor
            }
            "rainbow" -> {
                rainbowIW.borderWidth = 7
                rainbowIW.borderColor =  borderColor
            }

        }
    }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun onBlackThemeClick(view: View) {
        val switch :SwitchCompat = findViewById(R.id.myThemeSwitch)
        if (switch.isChecked) {
            //сохранение кнопок
            val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("darkTheme", true)
            editor.apply()
        }
        else{
            //сохранение кнопок
            val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("darkTheme", false)
            editor.apply()
        }
        val settingLayout : ConstraintLayout = findViewById(R.id.settingLayout)
        val underSwitchCL : ConstraintLayout = findViewById(R.id.underSwitchCL)
        val settingTW : TextView = findViewById(R.id.settingTW)
        val darkThemeTW : TextView = findViewById(R.id.darkThemeTW)
        val colorTW : TextView = findViewById(R.id.uraTW)
        val openIW : ImageView = findViewById(R.id.openIW)

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val darkTheme = sharedPref.getBoolean("darkTheme", false)
        if (darkTheme) {
            settingLayout.setBackgroundColor(Color.BLACK)
            underSwitchCL.setBackgroundResource(R.drawable.rounded_white_rectangle)
            settingTW.setTextColor(Color.WHITE)
            darkThemeTW.setTextColor(Color.WHITE)
            colorTW.setTextColor(Color.WHITE)
            openIW.setImageResource(R.drawable.open_for_dark_theme)
        }
        else{
            settingLayout.setBackgroundColor(Color.WHITE)
            underSwitchCL.setBackgroundResource(R.color.white)
            settingTW.setTextColor(Color.BLACK)
            darkThemeTW.setTextColor(Color.BLACK)
            colorTW.setTextColor(Color.BLACK)
            openIW.setImageResource(R.drawable.open)
        }

    }

    fun onRainbowStyle(view: View) {
        //сохранение кнопок
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("style(drawable)", R.drawable.gradient)
        editor.putString("style(string)", "rainbow")
        editor.apply()
        setInVisibleAndNotClickable()

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)
        strokeDesiredCircle()
    }

    fun onBlueStyle(view: View) {
        //сохранение кнопок
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("style(drawable)", R.drawable.blue)
        editor.putString("style(string)", "blue")
        editor.apply()
        setInVisibleAndNotClickable()

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)
        strokeDesiredCircle()
    }

    fun onGreenStyle(view: View) {
        //сохранение кнопок
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("style(drawable)", R.drawable.green)
        editor.putString("style(string)", "green")
        editor.apply()
        setInVisibleAndNotClickable()


        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)
        strokeDesiredCircle()
    }

    fun onRedStyle(view: View) {
        //сохранение кнопок
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("style(drawable)", R.drawable.red)
        editor.putString("style(string)", "red")
        editor.apply()
        setInVisibleAndNotClickable()

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)
        strokeDesiredCircle()
    }

    fun onPinkStyle(view: View) {
        //сохранение кнопок
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("style(drawable)", R.drawable.pink)
        editor.putString("style(string)", "pink")
        editor.apply()
        setInVisibleAndNotClickable()

        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getInt("style(drawable)", android.R.color.white)

        val thisStyleIW : ImageView = findViewById(R.id.thisStyle)
        thisStyleIW.setImageResource(style)
        strokeDesiredCircle()
    }

    fun onChangeComplexity(view: View){
        val resultCode = Activity.RESULT_CANCELED
        val resultIntent = Intent()

        setResult(resultCode, resultIntent)
        finish()
    }

}