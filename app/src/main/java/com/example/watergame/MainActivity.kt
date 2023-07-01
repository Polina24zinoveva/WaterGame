package com.example.watergame

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

private var progressValue = 1
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainLayout : ConstraintLayout = findViewById(R.id.mainLayout)
        val animationDrawable : AnimationDrawable = mainLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()


        val value : TextView = findViewById(R.id.valueTW)
        val seekBar: SeekBar = findViewById(R.id.seekbar)
        seekBar.setProgress(progressValue)
        value.text = progressValue.toString();

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = progress.toString();
                //value.setText(progress)
                progressValue = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {    }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {     }
        })

        /*val intent = Intent(this, Game9bottles::class.java)
        startActivity(intent)*/


    }

    fun onYes(view: View){
        when (progressValue) {
            1 -> {
                val intent = Intent(this, Game::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, Game4bottles::class.java)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(this, Game5bottles::class.java)
                startActivity(intent)
            }
            4 -> {
                val intent = Intent(this, Game6bottles::class.java)
                startActivity(intent)
            }
            5 -> {
                val intent = Intent(this, Game7bottles::class.java)
                startActivity(intent)
            }
            6 -> {
                val intent = Intent(this, Game8bottles::class.java)
                startActivity(intent)
            }
            7 -> {
                val intent = Intent(this, Game9bottles::class.java)
                startActivity(intent)
            }
        }
    }

}