package com.capstone.bloomy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.bloomy.databinding.ActivityErrorNoInternetConnectionBinding

class ErrorNoInternetConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorNoInternetConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorNoInternetConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTryAgainErrorNoInternetConnection.setOnClickListener {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
    }
}