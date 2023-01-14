package com.example.hangmangame.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //API LOGIN
        val call = Constants.getRetrofit()
            .create(HangmanApi::class.java)
            .getWords("hangman.php")
        Log.d(Constants.LOGTAG, "Request: ${call.request()}")

        CoroutineScope(Dispatchers.IO).launch {
            call.enqueue(object : Callback<Words> {
                override fun onResponse(call: Call<Words>, response: Response<Words>) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Datos: ${response.body().toString()}")

                    with(binding){
                        tvCategory.text = response.body()?.category
                        tvWord.text = response.body()?.word
                        binding.pb.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Words>, t: Throwable) {
                    binding.pb.visibility = View.GONE
                    Toast.makeText(
                        this@HangmanGame,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this, HangmanGame::class.java)
        startActivity(intent)
    }
}