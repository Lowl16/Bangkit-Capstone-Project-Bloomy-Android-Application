package com.capstone.bloomy.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.capstone.bloomy.R
import com.capstone.bloomy.data.model.UserModel
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.databinding.ActivitySignInBinding
import com.capstone.bloomy.ui.customview.EmailEditText
import com.capstone.bloomy.ui.viewmodel.AuthenticationViewModel
import com.capstone.bloomy.ui.viewmodel.UserPreferencesViewModel
import com.capstone.bloomy.ui.viewmodelfactory.AuthenticationViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.UserPreferencesViewModelFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val authenticationViewModelFactory: AuthenticationViewModelFactory = AuthenticationViewModelFactory.getInstance()
    private val authenticationViewModel: AuthenticationViewModel by viewModels { authenticationViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences: UserPreferences = UserPreferences.getInstance(application.dataStore)
        val userPreferencesViewModelFactory: UserPreferencesViewModelFactory = UserPreferencesViewModelFactory.getInstance(userPreferences)
        val userPreferencesViewModel: UserPreferencesViewModel by viewModels { userPreferencesViewModelFactory }

        binding.btnSignIn.setOnClickListener {
            val signIn = binding.btnSignIn
            val email = binding.etEmailSignIn.text.toString()
            val password = binding.etPasswordSignIn.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 8) {
                    showLoading(signIn, true)

                    authenticationViewModel.signIn(email, password)
                    authenticationViewModel.signInResponse.observe(this) { session ->
                        if (session != null) {
                            val userModel = UserModel(
                                session.token
                            )

                            userPreferencesViewModel.saveSession(userModel)

                            showLoading(signIn, false)

                            val mainIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        }
                    }

                    authenticationViewModel.invalidSignInResponse.observe(this) { invalidSignIn ->
                        val invalid = invalidSignIn?.message.toString()

                        if (invalidSignIn != null) {
                            showLoading(signIn, false)

                            Toast.makeText(this, invalid, Toast.LENGTH_SHORT).show()
                            authenticationViewModel.defaultSignIn()
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun showForgotPasswordDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.forgot_password_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvForgotPassword: TextView = dialog.findViewById(R.id.tv_forgot_password_dialog)
        val tvDescriptionForgotPassword: TextView = dialog.findViewById(R.id.tv_description_forgot_password_dialog)
        val btnCancel: Button = dialog.findViewById(R.id.btn_cancel_forgot_password_dialog)
        val btnSend: Button = dialog.findViewById(R.id.btn_forgot_password_dialog)
        val progressBar: ProgressBar = dialog.findViewById(R.id.progress_bar_forgot_password_dialog)
        val etEmail: EmailEditText = dialog.findViewById(R.id.et_email_forgot_password_dialog)

        btnSend.setOnClickListener {
            val email = etEmail.text.toString()

            if (email.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                tvForgotPassword.visibility = View.GONE
                tvDescriptionForgotPassword.visibility = View.GONE
                btnCancel.visibility = View.GONE
                btnSend.visibility = View.GONE
                etEmail.visibility = View.GONE

                authenticationViewModel.forgotPassword(email)
                authenticationViewModel.forgotPasswordResponse.observe(this@SignInActivity) { response ->
                    val error = response?.error
                    val message = response?.message.toString()

                    if (error != null) {
                        progressBar.visibility = View.GONE
                        tvForgotPassword.visibility = View.VISIBLE
                        tvDescriptionForgotPassword.visibility = View.VISIBLE
                        btnCancel.visibility = View.VISIBLE
                        btnSend.visibility = View.VISIBLE
                        etEmail.visibility = View.VISIBLE

                        if (error == true) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            authenticationViewModel.defaultForgotPassword()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            authenticationViewModel.defaultForgotPassword()

                            dialog.dismiss()
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLoading(signIn: Button, isLoading: Boolean) { signIn.text = if (!isLoading) getString(R.string.btn_sign_in) else getString(R.string.btn_loading) }
}