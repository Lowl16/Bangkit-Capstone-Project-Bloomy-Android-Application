package com.capstone.bloomy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.bloomy.databinding.ActivitySignUpSuccessBinding

class SignUpSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnContinueSignUpSuccess.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}