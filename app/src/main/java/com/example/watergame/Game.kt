package com.example.watergame

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Thread.sleep
import java.util.*


private const val countBottle = 3
private const val countEmptyBottle = 1
private const val countColors = countBottle - countEmptyBottle

//private val colors = arrayOf("#FF6200EE", "#FF9800")
private var darkTheme = false
private var colors = arrayOf("0", "0")
private val colorsRainbow = arrayOf("#174ea9", "#BC2F82")
private val colorsPink = arrayOf("#af2156", "#f5a3c6")
private val colorsBLue = arrayOf("#001b6d", "#82b3dc")
private val colorsGreen = arrayOf("#2d953e", "#005335")
private val colorsRed = arrayOf("#e60026", "#66000b")


//var colors = arrayOf("#FF6200EE", "#FF3700B3", "#FF018786", "#E91E63", "#FF9800", "#4CAF50", "#EBFF36")

private val bottles = mutableListOf(mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>())

private var level = 1


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


class Game : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        dropSound  = MediaPlayer.create(this, R.raw.water_pouring)


        val numberLevelTextView : TextView = findViewById(R.id.numberLevelTextView)

        //установка уровня
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        level = sharedPref.getInt("level",0)
        numberLevelTextView.setText(level.toString())

        val editor = sharedPref.edit()
        editor.putInt("activity", 1)
        editor.apply()

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

        if (outplayClick) {
            //распоковка нужной расстановки цветов
            firstBottleString = sharedPref.getString("firstBottleString", "").toString()
            secondBottleString = sharedPref.getString("secondBottleString", "").toString()

            bottles[0] = firstBottleString.split(',').toMutableList()
            bottles[1] = secondBottleString.split(',').toMutableList()

            for (i in 0..bottles.size - 1) {
                while (bottles[i].contains("")) {
                    bottles[i] -= ""
                }
            }

            //сохранение "я не хочу переиграть игру"
            editor.putBoolean("outplayClick", false)
            editor.apply()
        }
        else{
            val countColorsInBottle = mutableListOf(mutableListOf<Int>(), mutableListOf<Int>())
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
        }
        redrawing()

        //сохранение цветов бутылок на случай переигровки уровня
        //из цветов в строки
        firstBottleString = bottles[0].toString()
        secondBottleString = bottles[1].toString()

        firstBottleString = firstBottleString.replace("[", "").replace("]", "").replace(" ", "")
        secondBottleString = secondBottleString.replace("[", "").replace("]", "").replace(" ", "")

        editor.putString("firstBottleString", firstBottleString)
        editor.putString("secondBottleString", secondBottleString)
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun changeStyleAndTheme(){
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val style = sharedPref.getString("style(string)", "rainbow")

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


            //распоковка нужной расстановки цветов
            firstBottleString = sharedPref.getString("firstBottleString", "").toString()
            secondBottleString = sharedPref.getString("secondBottleString", "").toString()

            val bottlesWithNewStyleColors = mutableListOf(mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>())

            bottlesWithNewStyleColors[0] = firstBottleString.split(',').toMutableList()
            bottlesWithNewStyleColors[1] = secondBottleString.split(',').toMutableList()

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


            firstBottleString = firstBottleString.replace("[", "").replace("]", "").replace(" ", "")
            secondBottleString = secondBottleString.replace("[", "").replace("]", "").replace(" ", "")

            val editor = sharedPref.edit()
            editor.putString("firstBottleString", firstBottleString)
            editor.putString("secondBottleString", secondBottleString)
            editor.apply()
        }


        darkTheme = sharedPref.getBoolean("darkTheme", false)

        val gameLayout: ConstraintLayout = findViewById(R.id.game_layout)

        val picture1Bottle: ImageView = findViewById(R.id.bottle1)
        val picture2Bottle: ImageView = findViewById(R.id.bottle2)
        val picture3Bottle: ImageView = findViewById(R.id.bottle3)

        val numberLevelTextView : TextView = findViewById(R.id.numberLevelTextView)
        val textViewLevel : TextView = findViewById(R.id.textViewLevel)

        if (darkTheme){
            textViewLevel.setTextColor(Color.WHITE)
            numberLevelTextView.setTextColor(Color.WHITE)
            gameLayout.setBackgroundColor(Color.BLACK)
            picture1Bottle.setImageResource(R.drawable.black_bottle)
            picture2Bottle.setImageResource(R.drawable.black_bottle)
            picture3Bottle.setImageResource(R.drawable.black_bottle)

        }
        else{
            textViewLevel.setTextColor(Color.BLACK)
            numberLevelTextView.setTextColor(Color.BLACK)
            gameLayout.setBackgroundColor(Color.WHITE)
            picture1Bottle.setImageResource(R.drawable.bottle)
            picture2Bottle.setImageResource(R.drawable.bottle)
            picture3Bottle.setImageResource(R.drawable.bottle)
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

        if (countWinBottle == countColors)
        {
            level += 1
            //сохранение уровня
            val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("level", level)
            editor.commit()


            bottles[0].clear()
            bottles[1].clear()
            bottles[2].clear()

            supportFragmentManager.beginTransaction().replace(R.id.place_for_fragment, WinFragment1()).commit()
            /*val intent = Intent(this, WinActivity::class.java)
            startActivity(intent)*/
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
        var picture1Bottle: ConstraintLayout = findViewById(R.id.layoutBottle1)
        var picture2Bottle: ConstraintLayout = findViewById(R.id.layoutBottle2)
        var picture3Bottle: ConstraintLayout = findViewById(R.id.layoutBottle3)
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

        //сохранение "я хочу переиграть игру"
        val sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("outplayClick", true)
        editor.apply()

        val intent = Intent(this, Game::class.java)
        startActivity(intent)
    }

    fun onNewBottleButton(view: View)
    {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyAlertDialog)
        alert.setTitle("Чтобы получить дополнительную бутылку, решите пример")
        val a = (Random()).nextInt(101)
        val b = (Random()).nextInt(101)
        val o = (Random()).nextInt(2)

        /*val a = (-100..100).random()
        val b = (0..100).random()
        val o = (0..1).random()*/
        val colors = arrayOf("+", "-")
        val message = a.toString() + " " + colors[o] + " " + b.toString() + ":"
        var rightResult = 0
        when (o){
            0 -> rightResult = a + b
            1 -> rightResult = a - b
        }
        alert.setMessage(message)

        // Set an EditText view to get user input
        val inputResult = EditText(this)
        alert.setView(inputResult)
        inputResult.setTextSize(18F)

        alert.setPositiveButton("Ответить", DialogInterface.OnClickListener { dialog, whichButton ->
            if (inputResult.text.toString() == rightResult.toString())
            {
                dialog.cancel()

                val sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("newBottle", true)
                editor.apply()


                //переход к 4 бутылкам
                val intent = Intent(this, Game4bottles::class.java)
                startActivity(intent)
            }
            else{
                dialog.cancel()
                val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyAlertDialog)
                builder.setMessage("Это неправильный ответ :(")
                builder.setNeutralButton("Хорошо") { dialog, which ->
                    Toast.makeText(applicationContext,
                        "Maybe", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        })
        alert.setNegativeButton("Вернуться",
            DialogInterface.OnClickListener { dialog, whichButton ->
                // Canceled.
            })

        alert.show()
    }
}