package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var adv: TextView
    private lateinit var advbtn: Button

    val adUrl = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adv = findViewById(R.id.tv_ad)
        advbtn = findViewById(R.id.btnAD)

        advbtn.setOnClickListener() {

            requestApi()

        }
    }

    private fun requestApi() {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchRandomAdvice()

            }.await()

            if (data.isNotEmpty()) {

                updateAdviceText(data)
            }

        }

    }

    private fun fetchRandomAdvice(): String {

        var response = ""
        try {
            response = URL(adUrl).readText(Charsets.UTF_8)

        } catch (e: Exception) {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdviceText(data: String) {
        withContext(Dispatchers.Main)
        {

            val json = JSONObject(data)
            val slip = json.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            adv.text = advice

        }

    }
}

