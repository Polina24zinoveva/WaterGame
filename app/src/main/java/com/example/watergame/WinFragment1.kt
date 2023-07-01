package com.example.watergame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class WinFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref = this.activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref?.getInt("style(drawable)", android.R.color.white)

        val desiredActivity = sharedPref?.getInt("activity", 0)



        val view = inflater.inflate(R.layout.fragment_win1, container, false)
        val buttonDalshe : Button = view.findViewById(R.id.buttonDalshe)
        buttonDalshe.setOnClickListener{
            when (desiredActivity) {
                1 -> {
                    val intent = Intent(activity, Game::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(activity, Game4bottles::class.java)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(activity, Game5bottles::class.java)
                    startActivity(intent)
                }
                4 -> {
                    val intent = Intent(activity, Game6bottles::class.java)
                    startActivity(intent)
                }
                5 -> {
                    val intent = Intent(activity, Game7bottles::class.java)
                    startActivity(intent)
                }
                6 -> {
                    val intent = Intent(activity, Game8bottles::class.java)
                    startActivity(intent)
                }
                7 -> {
                    val intent = Intent(activity, Game9bottles::class.java)
                    startActivity(intent)
                }
            }
        }

        val changeComplexity : TextView = view.findViewById(R.id.changeComplexity)
        changeComplexity.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }


        /*val view = inflater.inflate(R.layout.fragment_win1, container, false)
        val buttonDalshe : Button = view.findViewById(R.id.buttonDalshe)
        buttonDalshe.setOnClickListener{
            val intent = Intent(activity, Game::class.java)
            startActivity(intent)
        }*/

        val uraCL : ConstraintLayout = view.findViewById(R.id.uraCL)
        if (style != null) {
            uraCL.setBackgroundResource(style)
        }

        // Inflate the layout for this fragment
        return view
    }

    companion object{
        @JvmStatic
        fun newInstance() = WinFragment1();
    }

}