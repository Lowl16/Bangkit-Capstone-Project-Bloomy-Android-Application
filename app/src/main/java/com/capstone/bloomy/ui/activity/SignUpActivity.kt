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
import com.capstone.bloomy.databinding.ActivitySignUpBinding
import com.capstone.bloomy.ui.customview.EmailEditText
import com.capstone.bloomy.ui.viewmodel.AuthenticationViewModel
import com.capstone.bloomy.ui.viewmodelfactory.AuthenticationViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val authenticationViewModelFactory: AuthenticationViewModelFactory = AuthenticationViewModelFactory.getInstance()
    private val authenticationViewModel: AuthenticationViewModel by viewModels { authenticationViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val username = binding.etUsernameSignUp.text.toString()
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()
            val confirmPassword = binding.etConfirmPasswordSignUp.text.toString()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length >= 8) {
                    if (confirmPassword.length >= 8) {
                        showSignUpDialog()
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.btnResendVerification.setOnClickListener {
            showSendVerificationLinkDialog()
        }
    }

    private fun showSignUpDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.sign_up_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnNoSignUp: Button = dialog.findViewById(R.id.btn_no_sign_up_dialog)
        val btnYesSignUp: Button = dialog.findViewById(R.id.btn_yes_sign_up_dialog)

        btnYesSignUp.setOnClickListener {
            val signUp = binding.btnSignUp
            val username = binding.etUsernameSignUp.text.toString()
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()
            val confirmPassword = binding.etConfirmPasswordSignUp.text.toString()

            showLoading(signUp, true)

            authenticationViewModel.signUp(email, username, password, confirmPassword)
            authenticationViewModel.signUpResponse.observe(this@SignUpActivity) { response ->
                val error = response?.error
                val message = response?.message.toString()

                if (error != null) {
                    showLoading(signUp, false)
                    if (error == true) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        authenticationViewModel.defaultSignUp()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        authenticationViewModel.defaultSignUp()

                        dialog.dismiss()

                        val signUpSuccessIntent = Intent(this, SignUpSuccessActivity::class.java)
                        signUpSuccessIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(signUpSuccessIntent)
                    }
                }
            }
        }

        btnNoSignUp.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSendVerificationLinkDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.send_verification_link_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvSendVerificationLink: TextView = dialog.findViewById(R.id.tv_send_verification_link_dialog)
        val tvDescriptionSendVerificationLink: TextView = dialog.findViewById(R.id.tv_description_send_verification_link_dialog)
        val btnCancel: Button = dialog.findViewById(R.id.btn_cancel_send_verification_link_dialog)
        val btnSend: Button = dialog.findViewById(R.id.btn_send_verification_link_dialog)
        val progressBar: ProgressBar = dialog.findViewById(R.id.progress_bar_send_verification_link_dialog)
        val etEmail: EmailEditText = dialog.findViewById(R.id.et_email_send_verification_link_dialog)

        btnSend.setOnClickListener {
            val email = etEmail.text.toString()

            if (email.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                tvSendVerificationLink.visibility = View.GONE
                tvDescriptionSendVerificationLink.visibility = View.GONE
                btnCancel.visibility = View.GONE
                btnSend.visibility = View.GONE
                etEmail.visibility = View.GONE

                authenticationViewModel.sendVerificationLink(email)
                authenticationViewModel.sendVerificationLinkResponse.observe(this@SignUpActivity) { response ->
                    val error = response?.error
                    val message = response?.message.toString()

                    if (error != null) {
                        progressBar.visibility = View.GONE
                        tvSendVerificationLink.visibility = View.VISIBLE
                        tvDescriptionSendVerificationLink.visibility = View.VISIBLE
                        btnCancel.visibility = View.VISIBLE
                        btnSend.visibility = View.VISIBLE
                        etEmail.visibility = View.VISIBLE

                        if (error == true) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            authenticationViewModel.defaultSendVerificationLink()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            authenticationViewModel.defaultSendVerificationLink()

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

    private fun showLoading(signUp: Button, isLoading: Boolean) { signUp.text = if (!isLoading) getString(R.string.btn_sign_up) else getString(R.string.btn_loading) }
}