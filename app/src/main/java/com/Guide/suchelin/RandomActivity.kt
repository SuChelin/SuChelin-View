package com.Guide.suchelin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Guide.suchelin.databinding.ActivityRandomBinding

class RandomActivity : AppCompatActivity() {
    val binding by lazy { ActivityRandomBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //random추천할거 띄워줌

        binding.goMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}