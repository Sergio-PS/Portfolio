package com.example.pruebaoptativa_20_12_2021_sergiopolo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
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

    private var playerTurn : String = if (player1Movements.size + player2Movements.size % 2 == 0) "red" else "yellow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("log", "executed")
    }

    fun gameLogic(view : View){
        Log.i("log", "id: " + view.context.resources.getResourceEntryName(view.id))
        animateCoinTurn(view)
        //updateLists()
        //checkEndGame()
    }

    fun animateCoinTurn(view: View){
        val myView : ImageView = view as ImageView

        val translationConstant : Float = 1000F
        myView.animate().translationY(-translationConstant).duration = 1 //move coin upwards so the user can't see the process
        myView.setImageResource(if(playerTurn.equals("red")) R.drawable.red else R.drawable.yellow) //change to corresponding image by turn
        /*
        //Define rotation animation
        val rotate = RotateAnimation(0F,
            180F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 5000
        rotate.interpolator = LinearInterpolator()
        */
        myView.animate().translationY(translationConstant).rotation(450F).duration = 1000
    }

    fun updateLists(){

    }

    fun checkEndGame(){
        if(checkIfWinner()) endGame(playerTurn)
        else if ((player1Movements.size + player2Movements.size) == 9) endGame("DRAW")
    }

    fun checkIfWinner() : Boolean {
        //loop to check if there is a winner by finding a posible combination that equals val sum
        return true
    }
    fun endGame(text : String){
        //animate winner or draw by parameter text
    }
}