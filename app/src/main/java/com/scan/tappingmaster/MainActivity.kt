package com.scan.tappingmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    internal lateinit var tapme : Button
    internal lateinit var gamescore : TextView
    internal lateinit var timeleft : TextView
    internal var score=0
    internal var gamestarted =false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown:Long=60000
    internal val countDownInterval:Long =1000
    internal var timeLeftOnTimer:Long=60000
    internal val TAG=MainActivity::class.java.simpleName

    companion object{
        private val SCORE_KEY="SCORE_KEY"
        private val TIME_LEFT_KEY="TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapme=findViewById<Button>(R.id.tap_me)
        gamescore=findViewById<TextView>(R.id.game_score)
        timeleft=findViewById<TextView>(R.id.time_left)
        //gamescore.text=getString(R.string.your_score,score.toString())
        //resetGame()
        if (savedInstanceState!=null){
            score=savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer=savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else {
            resetGame()
        }

        tapme.setOnClickListener { view ->
            val bounceAnimation=AnimationUtils.loadAnimation(this,R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }


    }

    private fun restoreGame(){
        gamescore.text=getString(R.string.your_score,score.toString())
        val restoredtime=timeLeftOnTimer/1000
        timeleft.text=getString(R.string.time_left,restoredtime.toString())

        countDownTimer=object :CountDownTimer(timeLeftOnTimer,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer=millisUntilFinished
                var timeLeft=millisUntilFinished/1000
                timeleft.text=getString(R.string.time_left,timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gamestarted=true


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_KEY,timeLeftOnTimer)
        countDownTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onSaveInstanceState:Saving Score:$score & Time Left: $timeLeftOnTimer")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.about) {
            showInfo()
        }
        return true
    }

    private fun showInfo()
    {
        val dialogtitle=getString(R.string.about_title)
        val dialogmessage=getString(R.string.about_message)
        val builder=AlertDialog.Builder(this)
        builder.setTitle(dialogtitle)
        builder.setMessage(dialogmessage)
        builder.create().show()


    }

    private fun resetGame(){
        score=0
        gamescore.text=getString(R.string.your_score,score.toString())
        val initialTimeLeft=initialCountDown/1000
        timeleft.text=getString(R.string.time_left,initialTimeLeft.toString())
        countDownTimer=object :CountDownTimer(initialCountDown,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer=millisUntilFinished
                val timeLeftt=millisUntilFinished/1000
                timeleft.text=getString(R.string.time_left,timeLeftt.toString())


            }

            override fun onFinish() {
                endGame()

            }
        }
        gamestarted=false
    }

    private fun startGame(){
        countDownTimer.start()
        gamestarted=true

    }

    private fun endGame(){
       //Toast.makeText(this,getString(R.string.game_over_message,score.toString()),Toast.LENGTH_SHORT.show())
        Toast.makeText(this,getString(R.string.game_over_message,score.toString()),Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun incrementScore()
    {
        if (!gamestarted){
            startGame()
        }
        score=score+1
        val newScore=getString(R.string.your_score,score.toString())
        gamescore.text=newScore
        val blinkanaimation=AnimationUtils.loadAnimation(this,R.anim.blink)
        gamescore.startAnimation(blinkanaimation)
    }


}
