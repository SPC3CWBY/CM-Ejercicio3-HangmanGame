package com.example.hangmangame.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.example.hangmangame.R
import com.example.hangmangame.databinding.ActivityHangmanGameBinding
import com.example.hangmangame.model.HangmanApi
import com.example.hangmangame.model.Words
import com.example.hangmangame.view.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HangmanGame : AppCompatActivity() {
    private lateinit var binding: ActivityHangmanGameBinding
    private lateinit var word: String
    private lateinit var underscoreWord: String
    private var tries: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvWinner.visibility = View.GONE
        binding.tvLosser.visibility = View.GONE
        binding.ivResetIcon.visibility = View.GONE

        //API LOGIN
        val call = Constants.getRetrofit()
            .create(HangmanApi::class.java)
            .getWords("hangman.php")
        Log.d(Constants.LOGTAG, "Request: ${call.request()}")

        // Background process
        CoroutineScope(Dispatchers.IO).launch {
            call.enqueue(object : Callback<Words> {
                override fun onResponse(call: Call<Words>, response: Response<Words>) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Datos: ${response.body().toString()}")

                    with(binding){
                        tvCategory.text = response.body()?.category
                        word = response.body()?.word.toString()
                        generateInitUnderscores()
                        tvWord.text = underscoreWord
                        binding.pb.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Words>, t: Throwable) {
                    binding.pb.visibility = View.GONE
                    Toast.makeText(
                        this@HangmanGame,
                        getString(R.string.error, "${t.message}"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    fun onClick(view: View) {

        when(view.id){
            R.id.ivResetIcon -> {
                val intent = Intent(this, HangmanGame::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.lettersLayout.children.forEach { letterView ->
            if (letterView is TextView){
                letterView.setOnClickListener {
                    println(letterView.text.get(0))//
                    game(letterView.text.get(0))

                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun game(userLetter: Char){
        updateWord(userLetter.lowercaseChar())
        gameState()
    }

    fun generateInitUnderscores() {
        val sb = StringBuilder()
        for (x in word){
           // Se puede modificar para en un futuro agregar dificultad
           if (x == 'a'|| x == 'i' || x == 'u'){
               sb.append(x)
           } else {
               sb.append("_")
           }
        }
        binding.tvA.visibility = View.GONE
        binding.tvI.visibility = View.GONE
        binding.tvU.visibility = View.GONE
        underscoreWord = sb.toString()
    }

    private fun updateWord(letterUser: Char){
        if (word.contains(letterUser)) {
            fillUnderscores(letterUser)
        } else {
            tries += 1
            errors()
        }
    }

    private fun fillUnderscores(letterUser: Char){
        val sb = StringBuilder()

        for (x in word){
            if (underscoreWord.contains(x)){
                // TRUE
                // TA BIEN
                sb.append(x)
            } else {
                // FALSE
                if (x == letterUser) {
                    sb.append(x)
                } else {
                    sb.append("_")
                }
            }
        }
        underscoreWord = sb.toString()
        binding.tvWord.text = underscoreWord
    }

    private fun gameState(){
        if (tries == 6){
            // GAME OVER
            binding.tvLosser.visibility = View.VISIBLE
            binding.lettersLayout.visibility = View.GONE
            binding.ivResetIcon.visibility = View.VISIBLE
        } else if (underscoreWord == word){
            // WIN GAME
            binding.tvWinner.visibility = View.VISIBLE
            binding.lettersLayout.visibility = View.GONE
            binding.ivResetIcon.visibility = View.VISIBLE
        }

    }

    private fun errors(){
        when(tries){
            1 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_1)
            }
            2 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_2)
            }
            3 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_3)
            }
            4 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_4)
            }
            5 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_5)
            }
            6 -> {
                binding.ivHangman3.setImageResource(R.drawable.hangman_6)
            }
        }
    }
}