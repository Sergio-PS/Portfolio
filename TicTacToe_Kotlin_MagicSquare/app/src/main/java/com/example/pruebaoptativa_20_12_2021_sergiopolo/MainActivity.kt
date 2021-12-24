package com.example.pruebaoptativa_20_12_2021_sergiopolo

import android.graphics.Color
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isInvisible
import java.util.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    /*
    formula cuadrado magico = n(n^2 + 1) / 2
    siendo n el numero de casillas por fila o columna. Como es cuadrado implica que n * f = n * c.
     */
    private val n : Int = 3
    private val sum : Double = (n * (n.toDouble().pow(2.toDouble()) + 1)) / 2

    private var player1Movements = mutableListOf<Int>()
    private var player2Movements = mutableListOf<Int>()

    private lateinit var playerTurn : String

    private var player1score : Int = 0
    private var player2score : Int = 0

    fun changePlayerTurnCheck(){
        playerTurn = if ((player1Movements.size + player2Movements.size) % 2 == 0) "red" else "yellow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gameLogic(v : View){
        val view : ImageView = v as ImageView
        Log.i("log", "id: " + view.context.resources.getResourceEntryName(view.id))

        changePlayerTurnCheck()
        animateCoinTurn(view)
        updateLists(view)
        checkEndGame()
    }
        //in
    fun animateCoinTurn(view : ImageView){
        val translationConstant : Float = 1000F
        view.y = view.y + (translationConstant * -1) //move coin upwards so the user can't see the process
        view.setImageResource(if(playerTurn.equals("red")) R.drawable.red else R.drawable.yellow) //change to corresponding image by turn
        view.animate().translationYBy(translationConstant).rotation(3600F).duration = 1000 //set animation downwards rotation
        //take off onclick
        view.setOnClickListener(null)
    }

    fun updateLists(view : ImageView){
        val tagString = view.tag
        val tagNumber = Integer.parseInt(tagString as String)

        when(playerTurn){
            "red" -> player1Movements.add(tagNumber)
            "yellow" -> player2Movements.add(tagNumber)
        }
    }

    fun checkEndGame(){
        if(checkIfWinner()) endGame(playerTurn)
        else if ((player1Movements.size + player2Movements.size) == 9) endGame("DRAW")
    }
            //in
    fun checkIfWinner() : Boolean {
        var array = if(playerTurn.equals("red")) player1Movements  else player2Movements

        //check for a sum that takes places using three chips
        for(i in 0 until array.size){
            for(j in (i + 1) until array.size){
                for(k in (j + 1) until array.size){
                    var sumPossibility : Double = array[i] + array[j] + array[k].toDouble()
                    if(sumPossibility == sum) return true
                }
            }
        }

        return false
    }

    fun endGame(text : String){
        //WIN / DRAW message animations
        var textView : TextView = findViewById<TextView>(R.id.result)
        when(text){
            "DRAW" -> {
                textView.text = text
                textView.setTextColor(Color.GREEN)
                //play song
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.draw);
                mediaPlayer.start()
            }
            "red" -> {
                textView.text = text.uppercase() + " WINS"
                textView.setTextColor(Color.RED)
                player1score++
                //play song
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.win);
                mediaPlayer.start()
            }
            "yellow" -> {
                textView.text = text.uppercase() + " WINS"
                textView.setTextColor(Color.parseColor("#ECD12A"))
                player2score++
                //play song
                var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.win);
                mediaPlayer.start()
            }
        }
        textView.isInvisible = false

        //restart button animations
        var restartButtonTextView : TextView = findViewById<TextView>(R.id.restartButton)
        restartButtonTextView.isInvisible = false

        //block game image views
        changeGameStateInteraction(false)
    }
                //in
    fun changeGameStateInteraction(condition : Boolean){
        var column1 : LinearLayout = findViewById<LinearLayout>(R.id.column1)
        var column2 : LinearLayout = findViewById<LinearLayout>(R.id.column2)
        var column3 : LinearLayout = findViewById<LinearLayout>(R.id.column3)
        setImageViewsCondition(column1, condition)
        setImageViewsCondition(column2, condition)
        setImageViewsCondition(column3, condition)
    }
                    //in
    fun setImageViewsCondition(column : LinearLayout, condition: Boolean){
        for(i in 0 until column.childCount){
            val subView: View = column.getChildAt(i)
            if (subView is ImageView) {
                val imageView = subView as ImageView
                imageView.isClickable = condition
            }
        }
    }
                    //out
                //out
            //out
        //out
    fun restartLogic(v : View){
        //reload layout, because layout functions are low coupled -> code below is posible
        setContentView(R.layout.activity_main)
        //set array logs to empty
        player1Movements = mutableListOf<Int>()
        player2Movements = mutableListOf<Int>()
    }
}