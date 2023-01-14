package com.example.hangmangame.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.hangmangame.databinding.ActivityHangmanGameBinding

class HangmanGame : AppCompatActivity() {
    private lateinit var binding: ActivityHangmanGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun onClick(view: View) {

    }
}