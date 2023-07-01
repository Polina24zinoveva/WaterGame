package com.example.watergame

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*

private const val countBottle = 10
private const val countEmptyBottle = 1
private const val countColors = countBottle - countEmptyBottle

//private val colors = arrayOf("#FF6200EE", "#FF9800")

private var darkTheme = false
private var colors = arrayOf("0", "0", "0", "0", "0", "0", "0", "0", "0")
private val colorsRainbow = arrayOf("#174ea9", "#BC2F82", "#f0ca3e", "#3fb39d", "#f24e4a", "#d07c1d", "#a2d883", "#e59a9d")
private val colorsPink = arrayOf("#af2156", "#f5a3c6", "#ec4a9b", "#db60a4", "#f8d0d0", "#822566", "#bc51ac", "#806a8e")
private val colorsBLue = arrayOf("#001b6d", "#82b3dc", "#009fd3", "#00507d", "#5861a4", "#004a55", "#534ED9", "#50dfdd")
private val colorsGreen = arrayOf("#2d953e", "#005335", "#c5e1a4", "#01ae81", "#004123", "#0b9051", "#37b97d", "#4f7a5e")
private val colorsRed = arrayOf("#e60026", "#66000b", "#dd6a52", "#b2243a", "#a73b2f", "#560122", "#ef5350", "#740d09")


private val bottles = mutableListOf(mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>())

private var level = 0

private var thisNewBottle = false


private var readyToPerelivat = false
private var countToPerelivat = 0
private var perelito = 0
private var colorToPerelivat = ""
private var bottleFromPerelivat = 0

private var dropSound: MediaPlayer? = null

//private var LC: MediaPlayer? = null

private var soundBool = true

private var firstBottleString = ""
private var secondBottleString = ""
private var thirdBottleString = ""
private var fourthBottleString = ""
private var fifthBottleString = ""
private var sixthBottleString = ""
private var seventhBottleString = ""
private var eighthBottleString = ""
private var ninthBottleString = ""

class Game10bottles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game10bottles)

        dropSound  = MediaPlayer.create(this, R.raw.water_pouring)


        val numberLevelTextView : TextView = findViewById(R.id.numberLevelTextView)

        //установка уровня
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        level = sharedPref.getInt("level",0)
        numberLevelTextView.setText(level.toString())

        //установка кнопки звука
        soundBool = sharedPref.getBoolean("sound",false)
        val soundButton : ImageButton = findViewById(R.id.soundButton)
        //LC  = MediaPlayer.create(this, R.raw.solnce_monaco)
        when (soundBool) {
            true -> { soundButton.setImageResource(R.drawable.sound)
                //LC?.run { start() }
            }
            false -> { soundButton.setImageResource(R.drawable.no_sound)
                //LC?.run { pause() }
            }
        }

        changeStyleAndTheme()

        //проверка на переигровку игры
        val outplayClick = sharedPref.getBoolean("outplayClick", false)

        //проверка на новую бутылку
        thisNewBottle = sharedPref.getBoolean("newBottle", false)
        if (!thisNewBottle){
            val editor = sharedPref.edit()
            editor.putInt("activity", 7)
            editor.apply()
        }

        if (outplayClick) {
            val sharedPref10 = getSharedPreferences("my_preferences10", Context.MODE_PRIVATE)
            //распоковка нужной расстановки цветов
            firstBottleString = sharedPref10.getString("firstBottleString", "").toString()
            secondBottleString = sharedPref10.getString("secondBottleString", "").toString()
            thirdBottleString = sharedPref10.getString("thirdBottleString", "").toString()
            fourthBottleString = sharedPref10.getString("fourthBottleString", "").toString()
            fifthBottleString = sharedPref10.getString("fifthBottleString", "").toString()
            sixthBottleString = sharedPref10.getString("sixthBottleString", "").toString()
            seventhBottleString = sharedPref10.getString("seventhBottleString", "").toString()
            eighthBottleString = sharedPref10.getString("eighthBottleString", "").toString()
            ninthBottleString = sharedPref10.getString("ninthBottleString", "").toString()


            bottles[0] = firstBottleString.split(',').toMutableList()
            bottles[1] = secondBottleString.split(',').toMutableList()
            bottles[2] = thirdBottleString.split(',').toMutableList()
            bottles[3] = fourthBottleString.split(',').toMutableList()
            bottles[4] = fifthBottleString.split(',').toMutableList()
            bottles[5] = sixthBottleString.split(',').toMutableList()
            bottles[6] = seventhBottleString.split(',').toMutableList()
            bottles[7] = eighthBottleString.split(',').toMutableList()
            bottles[8] = ninthBottleString.split(',').toMutableList()

            for (i in 0..bottles.size - 1) {
                while (bottles[i].contains("")) {
                    bottles[i] -= ""
                }
            }
            //сохранение "я не хочу переиграть игру"
            val editor = sharedPref.edit()
            editor.putBoolean("outplayClick", false)
            editor.apply()
        }
        else if (thisNewBottle){
            //распоковка нужной расстановки цветов из Game4bottles
            val sharedPref9 = getSharedPreferences("my_preferences9", Context.MODE_PRIVATE)

            firstBottleString = sharedPref9.getString("firstBottleString", "").toString()
            secondBottleString = sharedPref9.getString("secondBottleString", "").toString()
            thirdBottleString = sharedPref9.getString("thirdBottleString", "").toString()
            fourthBottleString = sharedPref9.getString("fourthBottleString", "").toString()
            fifthBottleString = sharedPref9.getString("fifthBottleString", "").toString()
            sixthBottleString = sharedPref9.getString("sixthBottleString", "").toString()
            seventhBottleString = sharedPref9.getString("seventhBottleString", "").toString()
            eighthBottleString = sharedPref9.getString("eighthBottleString", "").toString()

            bottles[0] = firstBottleString.split(',').toMutableList()
            bottles[1] = secondBottleString.split(',').toMutableList()
            bottles[2] = thirdBottleString.split(',').toMutableList()
            bottles[3] = fourthBottleString.split(',').toMutableList()
            bottles[4] = fifthBottleString.split(',').toMutableList()
            bottles[5] = sixthBottleString.split(',').toMutableList()
            bottles[6] = seventhBottleString.split(',').toMutableList()
            bottles[7] = eighthBottleString.split(',').toMutableList()



            //сохранение "я не хочу новую бутылку"
            val editor = sharedPref.edit()
            editor.putBoolean("newBottle", false)
            editor.apply()
        }
        /*else{
            val countColorsInBottle = mutableListOf(mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>())
            for (j in 0..countBottle - countEmptyBottle - 1) { //иду по бутылкам
                for (i in 0..3) {            //иду по ячейкам цвета(их 4)
                    val randomValue = (java.util.Random()).nextInt(countColors)
                    if (countColorsInBottle[randomValue].size < 3) {
                        bottles[j] += colors[randomValue]
                        countColorsInBottle[randomValue].add(j)
                    }
                    else if (countColorsInBottle[randomValue].size == 3 ){
                        if (countColorsInBottle[randomValue][0] == countColorsInBottle[randomValue][1]  &&  countColorsInBottle[randomValue][1] == countColorsInBottle[randomValue][2]  &&  countColorsInBottle[randomValue][0] == j)
                        {
                            for (k in 0..countColors - 1) { //иду по цветам
                                if (countColorsInBottle[k].size < 4  && k != randomValue){
                                    bottles[j] += colors[k]
                                    countColorsInBottle[k].add(j)
                                    break
                                }
                            }
                        }
                        else{
                            bottles[j] += colors[randomValue]
                            countColorsInBottle[randomValue].add(j)
                        }
                    }
                    else {           //если хотят добавить цвет в бутылку, где уже 4 цвета есть
                        for (k in 0..countColors - 1) { //иду по цветам
                            if (countColorsInBottle[k].size < 4  && k != randomValue){
                                bottles[j] += colors[k]
                                countColorsInBottle[k].add(j)
                                break
                            }
                        }
                    }
                }
            }
        }*/
        redrawing()

        //сохранение цветов бутылок на случай переигровки уровня
        //из цветов в строки
        firstBottleString = bottles[0].toString()
        secondBottleString = bottles[1].toString()
        thirdBottleString = bottles[2].toString()
        fourthBottleString = bottles[3].toString()
        fifthBottleString = bottles[4].toString()
        sixthBottleString = bottles[5].toString()
        seventhBottleString = bottles[6].toString()
        eighthBottleString = bottles[7].toString()
        ninthBottleString = bottles[8].toString()



        firstBottleString = firstBottleString.replace("[", "").replace("]", "").replace(" ", "")
        secondBottleString = secondBottleString.replace("[", "").replace("]", "").replace(" ", "")
        thirdBottleString = thirdBottleString.replace("[", "").replace("]", "").replace(" ", "")
        fourthBottleString = fourthBottleString.replace("[", "").replace("]", "").replace(" ", "")
        fifthBottleString = fifthBottleString.replace("[", "").replace("]", "").replace(" ", "")
        sixthBottleString = sixthBottleString.replace("[", "").replace("]", "").replace(" ", "")
        seventhBottleString = seventhBottleString.replace("[", "").replace("]", "").replace(" ", "")
        eighthBottleString = eighthBottleString.replace("[", "").replace("]", "").replace(" ", "")
        ninthBottleString = ninthBottleString.replace("[", "").replace("]", "").replace(" ", "")

        val sharedPref10 = getSharedPreferences("my_preferences10", Context.MODE_PRIVATE)
        val editor = sharedPref10.edit()
        editor.putString("firstBottleString", firstBottleString)
        editor.putString("secondBottleString", secondBottleString)
        editor.putString("thirdBottleString", thirdBottleString)
        editor.putString("fourthBottleString", fourthBottleString)
        editor.putString("fifthBottleString", fifthBottleString)
        editor.putString("sixthBottleString", sixthBottleString)
        editor.putString("seventhBottleString", seventhBottleString)
        editor.putString("eighthBottleString", eighthBottleString)
        editor.putString("ninthBottleString", ninthBottleString)

        editor.apply()
    }


    fun on1Bottle(view: View) {
        waterMovement(0)
    }

    fun on2Bottle(view: View) {
        waterMovement(1)
    }

    fun on3Bottle(view: View) {
        waterMovement(2)
    }

    fun on4Bottle(view: View) {
        waterMovement(3)
    }

    fun on5Bottle(view: View) {
        waterMovement(4)
    }

    fun on6Bottle(view: View) {
        waterMovement(5)
    }

    fun on7Bottle(view: View) {
        waterMovement(6)
    }

    fun on8Bottle(view: View) {
        waterMovement(7)
    }

    fun on9Bottle(view: View) {
        waterMovement(8)
    }
    fun on10Bottle(view: View) {
        waterMovement(9)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun onSound(view: View){
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()


        val soundButton : ImageButton = findViewById(R.id.soundButton)
        if (soundButton.drawable.constantState === resources.getDrawable(R.drawable.sound).constantState) {
            soundButton.setImageResource(R.drawable.no_sound)
            editor.putBoolean("sound", false)
            editor.apply()

            soundBool = false

            //LC?.run { pause() }
        }
        else if (soundButton.drawable.constantState === resources.getDrawable(R.drawable.no_sound).constantState) {
            soundButton.setImageResource(R.drawable.sound)
            editor.putBoolean("sound", true)
            editor.apply()

            soundBool = true

            //LC?.run { start() }
        }
    }

    fun onSettings(view: View){
        val requestCode = 1
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Ваш код для выполнения, когда Activity2 закрылось успешно
            changeStyleAndTheme()
        }
        else if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
            // Ваш код для выполнения, когда Activity2 закрылось успешно
            bottles[0].clear()
            bottles[1].clear()
            bottles[2].clear()
            bottles[3].clear()
            bottles[4].clear()
            bottles[5].clear()
            bottles[6].clear()
            bottles[7].clear()
            bottles[8].clear()
            bottles[9].clear()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeStyleAndTheme(){
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getString("style(string)", "")

        val oldColors = colors
        when (style) {
            "blue" -> { colors = colorsBLue }
            "green" -> { colors = colorsGreen }
            "red" -> { colors = colorsRed }
            "pink" -> { colors = colorsPink }
            "rainbow" -> { colors = colorsRainbow }
        }
        if (!colors.contentEquals(oldColors)) {
            for (i in 0 until bottles.size) {
                for (j in 0 until bottles[i].size) {
                    for (k in 0 until oldColors.size) {
                        if (bottles[i][j].contains(oldColors[k])) {
                            bottles[i][j] = colors[k]
                        }
                    }
                }
            }




            val sharedPref10 = getSharedPreferences("my_preferences10", Context.MODE_PRIVATE)
            //распоковка нужной расстановки цветов
            firstBottleString = sharedPref10.getString("firstBottleString", "").toString()
            secondBottleString = sharedPref10.getString("secondBottleString", "").toString()
            thirdBottleString = sharedPref10.getString("thirdBottleString", "").toString()
            fourthBottleString = sharedPref10.getString("fourthBottleString", "").toString()
            fifthBottleString = sharedPref10.getString("fifthBottleString", "").toString()
            sixthBottleString = sharedPref10.getString("sixthBottleString", "").toString()
            seventhBottleString = sharedPref10.getString("seventhBottleString", "").toString()
            eighthBottleString = sharedPref10.getString("eighthBottleString", "").toString()
            ninthBottleString = sharedPref10.getString("ninthBottleString", "").toString()


            val bottlesWithNewStyleColors = mutableListOf(mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>())

            bottlesWithNewStyleColors[0] = firstBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[1] = secondBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[2] = thirdBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[3] = fourthBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[4] = fifthBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[5] = sixthBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[6] = seventhBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[7] = eighthBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[8] = ninthBottleString.split(',').toMutableList()


            for (i in 0 until bottlesWithNewStyleColors.size) {
                for (j in 0 until bottlesWithNewStyleColors[i].size) {
                    for (k in 0 until oldColors.size) {
                        if (bottlesWithNewStyleColors[i][j].contains(oldColors[k])) {
                            bottlesWithNewStyleColors[i][j] = colors[k]
                        }
                    }
                }
            }

            //сохранение цветов нового стиля на случай переигровки уровня
            //из цветов в строки
            firstBottleString = bottlesWithNewStyleColors[0].toString()
            secondBottleString = bottlesWithNewStyleColors[1].toString()
            thirdBottleString = bottlesWithNewStyleColors[2].toString()
            fourthBottleString = bottlesWithNewStyleColors[3].toString()
            fifthBottleString = bottlesWithNewStyleColors[4].toString()
            sixthBottleString = bottlesWithNewStyleColors[5].toString()
            seventhBottleString = bottlesWithNewStyleColors[6].toString()
            eighthBottleString = bottlesWithNewStyleColors[7].toString()
            ninthBottleString = bottlesWithNewStyleColors[8].toString()



            firstBottleString = firstBottleString.replace("[", "").replace("]", "").replace(" ", "")
            secondBottleString = secondBottleString.replace("[", "").replace("]", "").replace(" ", "")
            thirdBottleString = thirdBottleString.replace("[", "").replace("]", "").replace(" ", "")
            fourthBottleString = fourthBottleString.replace("[", "").replace("]", "").replace(" ", "")
            fifthBottleString = fifthBottleString.replace("[", "").replace("]", "").replace(" ", "")
            sixthBottleString = sixthBottleString.replace("[", "").replace("]", "").replace(" ", "")
            seventhBottleString = seventhBottleString.replace("[", "").replace("]", "").replace(" ", "")
            eighthBottleString = eighthBottleString.replace("[", "").replace("]", "").replace(" ", "")
            ninthBottleString = ninthBottleString.replace("[", "").replace("]", "").replace(" ", "")

            val editor = sharedPref10.edit()
            editor.putString("firstBottleString", firstBottleString)
            editor.putString("secondBottleString", secondBottleString)
            editor.putString("thirdBottleString", thirdBottleString)
            editor.putString("fourthBottleString", fourthBottleString)
            editor.putString("fifthBottleString", fifthBottleString)
            editor.putString("sixthBottleString", sixthBottleString)
            editor.putString("seventhBottleString", seventhBottleString)
            editor.putString("eighthBottleString", eighthBottleString)
            editor.putString("ninthBottleString", ninthBottleString)

            editor.apply()
        }


        darkTheme = sharedPref.getBoolean("darkTheme", false)

        val gameLayout: ConstraintLayout = findViewById(R.id.game_layout)

        val picture1Bottle: ImageView = findViewById(R.id.bottle1)
        val picture2Bottle: ImageView = findViewById(R.id.bottle2)
        val picture3Bottle: ImageView = findViewById(R.id.bottle3)
        val picture4Bottle: ImageView = findViewById(R.id.bottle4)
        val picture5Bottle: ImageView = findViewById(R.id.bottle5)
        val picture6Bottle: ImageView = findViewById(R.id.bottle6)
        val picture7Bottle: ImageView = findViewById(R.id.bottle7)
        val picture8Bottle: ImageView = findViewById(R.id.bottle8)
        val picture9Bottle: ImageView = findViewById(R.id.bottle10)
        val picture10Bottle: ImageView = findViewById(R.id.bottle9)



        val numberLevelTextView : TextView = findViewById(R.id.numberLevelTextView)
        val textViewLevel : TextView = findViewById(R.id.textViewLevel)

        if (darkTheme){
            textViewLevel.setTextColor(Color.WHITE)
            numberLevelTextView.setTextColor(Color.WHITE)
            gameLayout.setBackgroundColor(Color.BLACK)
            picture1Bottle.setImageResource(R.drawable.black_bottle)
            picture2Bottle.setImageResource(R.drawable.black_bottle)
            picture3Bottle.setImageResource(R.drawable.black_bottle)
            picture4Bottle.setImageResource(R.drawable.black_bottle)
            picture5Bottle.setImageResource(R.drawable.black_bottle)
            picture6Bottle.setImageResource(R.drawable.black_bottle)
            picture7Bottle.setImageResource(R.drawable.black_bottle)
            picture8Bottle.setImageResource(R.drawable.black_bottle)
            picture9Bottle.setImageResource(R.drawable.black_bottle)
            picture10Bottle.setImageResource(R.drawable.black_bottle)

        }
        else{
            textViewLevel.setTextColor(Color.BLACK)
            numberLevelTextView.setTextColor(Color.BLACK)
            gameLayout.setBackgroundColor(Color.WHITE)
            picture1Bottle.setImageResource(R.drawable.bottle)
            picture2Bottle.setImageResource(R.drawable.bottle)
            picture3Bottle.setImageResource(R.drawable.bottle)
            picture4Bottle.setImageResource(R.drawable.bottle)
            picture5Bottle.setImageResource(R.drawable.bottle)
            picture6Bottle.setImageResource(R.drawable.bottle)
            picture7Bottle.setImageResource(R.drawable.bottle)
            picture8Bottle.setImageResource(R.drawable.bottle)
            picture9Bottle.setImageResource(R.drawable.bottle)
            picture10Bottle.setImageResource(R.drawable.bottle)

        }
        redrawing()
    }

    private fun redrawing(){

        val b1s1: ConstraintLayout = findViewById(R.id.b1s1)
        val b1s2: ConstraintLayout = findViewById(R.id.b1s2)
        val b1s3: ConstraintLayout = findViewById(R.id.b1s3)
        val b1s4: ConstraintLayout = findViewById(R.id.b1s4)

        val b2s1: ConstraintLayout = findViewById(R.id.b2s1)
        val b2s2: ConstraintLayout = findViewById(R.id.b2s2)
        val b2s3: ConstraintLayout = findViewById(R.id.b2s3)
        val b2s4: ConstraintLayout = findViewById(R.id.b2s4)

        val b3s1: ConstraintLayout = findViewById(R.id.b3s1)
        val b3s2: ConstraintLayout = findViewById(R.id.b3s2)
        val b3s3: ConstraintLayout = findViewById(R.id.b3s3)
        val b3s4: ConstraintLayout = findViewById(R.id.b3s4)

        val b4s1: ConstraintLayout = findViewById(R.id.b4s1)
        val b4s2: ConstraintLayout = findViewById(R.id.b4s2)
        val b4s3: ConstraintLayout = findViewById(R.id.b4s3)
        val b4s4: ConstraintLayout = findViewById(R.id.b4s4)

        val b5s1: ConstraintLayout = findViewById(R.id.b5s1)
        val b5s2: ConstraintLayout = findViewById(R.id.b5s2)
        val b5s3: ConstraintLayout = findViewById(R.id.b5s3)
        val b5s4: ConstraintLayout = findViewById(R.id.b5s4)

        val b6s1: ConstraintLayout = findViewById(R.id.b6s1)
        val b6s2: ConstraintLayout = findViewById(R.id.b6s2)
        val b6s3: ConstraintLayout = findViewById(R.id.b6s3)
        val b6s4: ConstraintLayout = findViewById(R.id.b6s4)

        val b7s1: ConstraintLayout = findViewById(R.id.b7s1)
        val b7s2: ConstraintLayout = findViewById(R.id.b7s2)
        val b7s3: ConstraintLayout = findViewById(R.id.b7s3)
        val b7s4: ConstraintLayout = findViewById(R.id.b7s4)

        val b8s1: ConstraintLayout = findViewById(R.id.b8s1)
        val b8s2: ConstraintLayout = findViewById(R.id.b8s2)
        val b8s3: ConstraintLayout = findViewById(R.id.b8s3)
        val b8s4: ConstraintLayout = findViewById(R.id.b8s4)

        val b9s1: ConstraintLayout = findViewById(R.id.b9s1)
        val b9s2: ConstraintLayout = findViewById(R.id.b9s2)
        val b9s3: ConstraintLayout = findViewById(R.id.b9s3)
        val b9s4: ConstraintLayout = findViewById(R.id.b9s4)

        val b10s1: ConstraintLayout = findViewById(R.id.b10s1)
        val b10s2: ConstraintLayout = findViewById(R.id.b10s2)
        val b10s3: ConstraintLayout = findViewById(R.id.b10s3)
        val b10s4: ConstraintLayout = findViewById(R.id.b10s4)

        if (darkTheme == false){
            for (i in 0..bottles.size - 1) {
                while (bottles[i].size < 4){
                    bottles[i] += "#FFFFFFFF"
                }
            }
        }
        else{
            for (i in 0..bottles.size - 1) {
                while (bottles[i].size < 4){
                    bottles[i] += "#FF000000"
                }
            }
        }

        b1s1.setBackgroundColor(Color.parseColor(bottles[0][3]))
        b1s2.setBackgroundColor(Color.parseColor(bottles[0][2]))
        b1s3.setBackgroundColor(Color.parseColor(bottles[0][1]))
        b1s4.setBackgroundColor(Color.parseColor(bottles[0][0]))

        b2s1.setBackgroundColor(Color.parseColor(bottles[1][3]))
        b2s2.setBackgroundColor(Color.parseColor(bottles[1][2]))
        b2s3.setBackgroundColor(Color.parseColor(bottles[1][1]))
        b2s4.setBackgroundColor(Color.parseColor(bottles[1][0]))

        b3s1.setBackgroundColor(Color.parseColor(bottles[2][3]))
        b3s2.setBackgroundColor(Color.parseColor(bottles[2][2]))
        b3s3.setBackgroundColor(Color.parseColor(bottles[2][1]))
        b3s4.setBackgroundColor(Color.parseColor(bottles[2][0]))

        b4s1.setBackgroundColor(Color.parseColor(bottles[3][3]))
        b4s2.setBackgroundColor(Color.parseColor(bottles[3][2]))
        b4s3.setBackgroundColor(Color.parseColor(bottles[3][1]))
        b4s4.setBackgroundColor(Color.parseColor(bottles[3][0]))

        b5s1.setBackgroundColor(Color.parseColor(bottles[4][3]))
        b5s2.setBackgroundColor(Color.parseColor(bottles[4][2]))
        b5s3.setBackgroundColor(Color.parseColor(bottles[4][1]))
        b5s4.setBackgroundColor(Color.parseColor(bottles[4][0]))

        b6s1.setBackgroundColor(Color.parseColor(bottles[5][3]))
        b6s2.setBackgroundColor(Color.parseColor(bottles[5][2]))
        b6s3.setBackgroundColor(Color.parseColor(bottles[5][1]))
        b6s4.setBackgroundColor(Color.parseColor(bottles[5][0]))

        b7s1.setBackgroundColor(Color.parseColor(bottles[6][3]))
        b7s2.setBackgroundColor(Color.parseColor(bottles[6][2]))
        b7s3.setBackgroundColor(Color.parseColor(bottles[6][1]))
        b7s4.setBackgroundColor(Color.parseColor(bottles[6][0]))

        b8s1.setBackgroundColor(Color.parseColor(bottles[7][3]))
        b8s2.setBackgroundColor(Color.parseColor(bottles[7][2]))
        b8s3.setBackgroundColor(Color.parseColor(bottles[7][1]))
        b8s4.setBackgroundColor(Color.parseColor(bottles[7][0]))

        b9s1.setBackgroundColor(Color.parseColor(bottles[8][3]))
        b9s2.setBackgroundColor(Color.parseColor(bottles[8][2]))
        b9s3.setBackgroundColor(Color.parseColor(bottles[8][1]))
        b9s4.setBackgroundColor(Color.parseColor(bottles[8][0]))

        b10s1.setBackgroundColor(Color.parseColor(bottles[9][3]))
        b10s2.setBackgroundColor(Color.parseColor(bottles[9][2]))
        b10s3.setBackgroundColor(Color.parseColor(bottles[9][1]))
        b10s4.setBackgroundColor(Color.parseColor(bottles[9][0]))

        if (darkTheme == false){
            for (i in 0..bottles.size - 1) {
                while (bottles[i].contains("#FFFFFFFF")) {
                    bottles[i] -= "#FFFFFFFF"
                }
            }
        }
        else{
            for (i in 0..bottles.size - 1) {
                while (bottles[i].contains("#FF000000")) {
                    bottles[i] -= "#FF000000"
                }
            }
        }


    }

    private fun winCheck(){
        var countWinBottle = 0
        var firstColor = ""
        var thisWinBottle = true

        for (i in 0..bottles.size - 1) {
            if (bottles[i].size == 4) {
                firstColor = bottles[i][0]
                for (j in 0..3) {
                    if (bottles[i][j] != firstColor) { thisWinBottle = false }
                }
                if (thisWinBottle == true) {countWinBottle += 1}
            }
            thisWinBottle = true
        }

        if ((!thisNewBottle && countWinBottle == countColors) ||(thisNewBottle && countWinBottle == countColors - 1))
        {
            level += 1
            //сохранение уровня
            val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("level", level)
            editor.apply()


            bottles[0].clear()
            bottles[1].clear()
            bottles[2].clear()
            bottles[3].clear()
            bottles[4].clear()
            bottles[5].clear()
            bottles[6].clear()
            bottles[7].clear()
            bottles[8].clear()
            bottles[9].clear()


            supportFragmentManager.beginTransaction().replace(R.id.place_for_fragment, WinFragment1()).commit()
        }
    }

    private fun waterMovement(thisBottle : Int){
        if (soundBool) { dropSound?.start() }


        if (readyToPerelivat == false)
        {
            dropSound?.pause()
            bottleFromPerelivat = thisBottle
            countToPerelivat = 0
            readyToPerelivat = true
            if (bottles[bottleFromPerelivat].size >= 1 ){
                colorToPerelivat = bottles[bottleFromPerelivat][bottles[bottleFromPerelivat].size - 1]

                for (i in bottles[bottleFromPerelivat].size - 1 downTo  0) {
                    if (bottles[bottleFromPerelivat][i] == colorToPerelivat) {
                        countToPerelivat ++
                    }
                    else { break }
                }
            }
            movingPicture(bottleFromPerelivat, "up", "")

        }
        else if (readyToPerelivat == true)
        {
            readyToPerelivat = false
            if (bottleFromPerelivat == thisBottle) {movingPicture(bottleFromPerelivat,"down", "")
                dropSound?.pause()
            }
            else if (bottles[thisBottle].size < 4)
            {
                Thread.sleep(500)
                while (bottles[thisBottle].size < 4  && countToPerelivat > 0) {
                    bottles[thisBottle] += colorToPerelivat
                    perelito ++
                    countToPerelivat --
                }
                while (perelito > 0) {
                    bottles[bottleFromPerelivat].removeAt(bottles[bottleFromPerelivat].size - 1)
                    perelito -= 1
                }
                movingPicture(bottleFromPerelivat, "down", "right")

                perelito = 0
                countToPerelivat = 0
                redrawing()
                winCheck()
            }
            else if (bottles[thisBottle].size >= 4) {
                dropSound?.pause()
                movingPicture(bottleFromPerelivat, "down", "")
            }
        }
    }

    private fun movingPicture(numberBottle : Int, directionUD : String, directionLR: String) {
        val picture1Bottle: ConstraintLayout = findViewById(R.id.layoutBottle1)
        val picture2Bottle: ConstraintLayout = findViewById(R.id.layoutBottle2)
        val picture3Bottle: ConstraintLayout = findViewById(R.id.layoutBottle3)
        val picture4Bottle: ConstraintLayout = findViewById(R.id.layoutBottle4)
        val picture5Bottle: ConstraintLayout = findViewById(R.id.layoutBottle5)
        val picture6Bottle: ConstraintLayout = findViewById(R.id.layoutBottle6)
        val picture7Bottle: ConstraintLayout = findViewById(R.id.layoutBottle7)
        val picture8Bottle: ConstraintLayout = findViewById(R.id.layoutBottle8)
        val picture9Bottle: ConstraintLayout = findViewById(R.id.layoutBottle9)
        val picture10Bottle: ConstraintLayout = findViewById(R.id.layoutBottle10)


        when (numberBottle) {
            0 -> {
                when (directionUD) {
                    "up" -> {
                        picture1Bottle.top -= 100
                        /*when (directionLR){
                            "left" -> {picture1Bottle.animate().rotation(30f)}
                            "right" -> {picture1Bottle.animate().rotation(-30f)}
                        }*/
                    }
                    "down" -> {
                        picture1Bottle.top += 100
                        /*when (directionLR){
                            "left" -> {picture1Bottle.animate().rotation(30f)}
                            "right" -> {picture1Bottle.animate().rotation(-30f)}
                        }*/
                    }
                }
            }
            1 -> {
                when (directionUD) {
                    "up" -> {
                        picture2Bottle.top -= 100
                        /*when (directionLR){
                            "left" -> {picture2Bottle.animate().rotation(30f)}
                            "right" -> {picture2Bottle.animate().rotation(-30f)}
                        }*/
                    }
                    "down" -> {
                        picture2Bottle.top += 100
                        /*when (directionLR){
                            "left" -> {picture2Bottle.animate().rotation(30f)}
                            "right" -> {picture2Bottle.animate().rotation(-30f)}
                        }*/
                    }
                }
            }
            2 -> {
                when (directionUD) {
                    "up" -> {
                        picture3Bottle.top -= 100
                        /*when (directionLR){
                            "left" -> {picture3Bottle.animate().rotation(30f)}
                            "right" -> {picture3Bottle.animate().rotation(-30f)}
                        }*/
                    }
                    "down" -> {
                        picture3Bottle.top += 100
                        /*when (directionLR){
                            "left" -> {picture3Bottle.animate().rotation(30f)}
                            "right" -> {picture3Bottle.animate().rotation(-30f)}
                        }*/
                    }
                }
            }
            3 -> {
                when (directionUD) {
                    "up" -> {
                        picture4Bottle.top -= 100
                    }
                    "down" -> {
                        picture4Bottle.top += 100
                    }
                }
            }
            4 -> {
                when (directionUD) {
                    "up" -> {
                        picture5Bottle.top -= 100
                    }
                    "down" -> {
                        picture5Bottle.top += 100
                    }
                }
            }
            5 -> {
                when (directionUD) {
                    "up" -> {
                        picture6Bottle.top -= 100
                    }
                    "down" -> {
                        picture6Bottle.top += 100
                    }
                }
            }
            6 -> {
                when (directionUD) {
                    "up" -> {
                        picture7Bottle.top -= 100
                    }
                    "down" -> {
                        picture7Bottle.top += 100
                    }
                }
            }
            7 -> {
                when (directionUD) {
                    "up" -> {
                        picture8Bottle.top -= 100
                    }
                    "down" -> {
                        picture8Bottle.top += 100
                    }
                }
            }
            8 -> {
                when (directionUD) {
                    "up" -> {
                        picture9Bottle.top -= 100
                    }
                    "down" -> {
                        picture9Bottle.top += 100
                    }
                }
            }
            9 -> {
                when (directionUD) {
                    "up" -> {
                        picture10Bottle.top -= 100
                    }
                    "down" -> {
                        picture10Bottle.top += 100
                    }
                }
            }
            else -> {
                throw Exception("Wrong bottle number")
            }
        }
    }

    fun onOutplayButton(view: View)
    {
        bottles[0].clear()
        bottles[1].clear()
        bottles[2].clear()
        bottles[3].clear()
        bottles[4].clear()
        bottles[5].clear()
        bottles[6].clear()
        bottles[7].clear()
        bottles[8].clear()
        bottles[9].clear()


        //сохранение "я хочу переиграть игру"
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("outplayClick", true)
        editor.apply()

        if (thisNewBottle){
            //сохранение "это новуя бутылка"
            val editor = sharedPref.edit()
            editor.putBoolean("newBottle", true)
            editor.apply()
        }

        val intent = Intent(this, Game10bottles::class.java)
        startActivity(intent)
    }

    fun onNewBottleButton(view: View)
    {
        val builder = AlertDialog.Builder(this,  R.style.MyAlertDialog)
        builder.setMessage("Для данного уровня больше нет доступных бутылок")
        builder.setNeutralButton("Хорошо") { dialog, which ->
            Toast.makeText(applicationContext,
                "Maybe", Toast.LENGTH_SHORT).show()
        }
        builder.show()
        return
    }

}
